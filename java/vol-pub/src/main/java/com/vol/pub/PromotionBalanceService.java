package com.vol.pub;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.PromotionBalance;
import com.vol.common.user.Bonus;
import com.vol.dao.AbstractQueryService;

public class PromotionBalanceService extends AbstractQueryService<Integer,PromotionBalance>{
	private final static long startup=System.currentTimeMillis();
	
	private String hostId;
	@Resource(name = "promotionBalanceDao")
	protected DAO<Integer, PromotionBalance> promotionBalanceDAO;
	
	protected DAO<Integer, PromotionHostBalance> promotionHostBalanceDAO;
	
	@Resource(name = "bonusDao")
	protected DAO<Long, Bonus> bonusDao;
	
	private ConcurrentMap<Integer,BalanceControl> cache = new ConcurrentHashMap<Integer,BalanceControl> ();

	@Override
	protected DAO<Integer, PromotionBalance> getDAO() {
		return promotionBalanceDAO;
	}
	
	
	public Bonus grant(Integer promotionId, int userId, String userName, long size){
		BalanceControl control = cache.get(promotionId);
		if(control == null){
			return null;
		}
		return control.grant(userId, userName, size);
	}
	
	
	public boolean load(Integer promotionId){
		BalanceControl control = new BalanceControl();
		//
		
		cache.put(promotionId, control);
		return false;
	}

	
	public boolean unload(Integer promotionId){
		BalanceControl control = cache.remove(promotionId);
		control.flushTask();
		return false;
	}
	
	
	public void sync(){
		for(Entry<Integer, BalanceControl> entry:cache.entrySet()){
			BalanceControl control = entry.getValue();
			control.flushTask();
			control.reserveTask(100);
		}
	}
	
	
	
	private class BalanceControl {
		private int balanceId;
		private int hostBalanceId;
		private long balance = 0;
		private final AtomicLong maximum = new AtomicLong();
		private List<Bonus> pendingBonus = new LinkedList<Bonus>();
		private final Lock lock = new ReentrantLock();
		private boolean isFinal;
		
		
		public Bonus grant(int userId, String userName, long size){
			lock.lock();
			try{
				if(balance <= 0){
					return null;
				}
				long balance2 = balance - size;
				if(balance2 < 0){
					if(isFinal){
						size = balance;
						balance = 0;
					}else{
						return null;
					}
				}
			
				Bonus bonus = new Bonus();
				bonus.setSize(size);
				pendingBonus.add(bonus);
				return bonus;
			}finally{
				lock.unlock();
			}		
		
		}
		
		
		public void flushTask(){
			final List<Bonus> pendingBonus;
			lock.lock();
			try{
				pendingBonus = this.pendingBonus;
				this.pendingBonus = new LinkedList<Bonus>();
			}finally{
				lock.unlock();
			}
			
			long used=0;
			for(Bonus bonus: pendingBonus){
				used+=bonus.getSize();
			}
			maximum.addAndGet(-used);

			for(Bonus bonus: pendingBonus){
				bonusDao.create(bonus);
			}
			// update host balance
			// update promotion balance;
			Map<String,Object> parameters = new HashMap<String,Object>();
			// update promotion balance;
			parameters.put("id", balanceId);
			parameters.put("current", System.currentTimeMillis());
			parameters.put("delta", used);
			promotionBalanceDAO.batchUpdate("promotionBalance.commit", parameters);
			
			parameters.put("id", hostBalanceId);
			parameters.put("current", System.currentTimeMillis());
			parameters.put("delta", -used);
			promotionHostBalanceDAO.batchUpdate("hostBalance.delta", parameters);
			
		}
		
		
		public void reserveTask(final long expect){
			long toReserve = expect - maximum.get();
			if(toReserve > 0){
				Map<String,Object> parameters = new HashMap<String,Object>();
				// update promotion balance;
				parameters.put("id", balanceId);
				parameters.put("current", System.currentTimeMillis());
				parameters.put("delta", toReserve);
				int rc = promotionBalanceDAO.batchUpdate("promotionBalance.reserve", parameters);
				if(rc != 1){
					isFinal = true;
					PromotionBalance balance = promotionBalanceDAO.get(balanceId);
					long left = balance.getBalance() - balance.getReserved();
					if(left > 0){
						toReserve = left;
					}else{
						return;
					}
				}
				
				// update host balance
				parameters.put("id", hostBalanceId);
				parameters.put("current", System.currentTimeMillis());
				parameters.put("delta", toReserve);
				promotionHostBalanceDAO.batchUpdate("hostBalance.delta", parameters);
			
			}
		}
	}
}
