package com.vol.pub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Promotion;
import com.vol.dao.AbstractQueryService;

public class PromotionService  extends AbstractQueryService<Integer,Promotion>  {
	
	private ScheduledExecutorService scheduler;

	@Resource(name="promotionDao")
	protected DAO<Integer,Promotion> promotionDAO;
	
	protected final ConcurrentMap<Integer,Promotion> cache = new ConcurrentHashMap<Integer,Promotion>(); 
	
	protected final ConcurrentMap<Integer,LockableValue<List<Promotion>>> cache2 = new ConcurrentHashMap<Integer,LockableValue<List<Promotion>>>(); 
	
	private ScheduledFuture<?> task;
	
	@Override
	protected DAO<Integer, Promotion> getDAO() {
		return promotionDAO;
	}
	
	public void init(){
		super.init();
		Runnable refreshJob = new Runnable(){
			@Override
			public void run() {
				refresh();
			}
		};
		task = scheduler.scheduleAtFixedRate(refreshJob, 300, 600,TimeUnit.SECONDS);
	}
	
	public void destroy(){
		if(task!=null){
			task.cancel(true);
		}
	}
	
	
	
	@Override
	public Promotion get(Integer id) {
		return cache.get(id);
	}
	
	
	public List<Promotion> getByTenant(Integer tenantId){
		LockableValue<List<Promotion>> lv = cache2.get(tenantId);
		return lv == null? null : lv.getObj();
	}
	
	
	public void refresh(){
		for(Entry<Integer, LockableValue<List<Promotion>>> entry: cache2.entrySet()){
			Integer tenantId = entry.getKey();
			LockableValue<List<Promotion>> value = entry.getValue();
			Lock lock = value.getLock();
			try{
				lock.lock();
				refresh(tenantId,value);
			}finally{
				lock.unlock();
			}
		}
	}
	
	public void load(Integer tenantId){
		LockableValue<List<Promotion>> value = new LockableValue<List<Promotion>>();
		refresh(tenantId,value); 
		LockableValue<List<Promotion>> value2 = cache2.putIfAbsent(tenantId, value);
		if(value2 != null){
			//abnormal
		}
	}
	
	public void unload(Integer tenantId){
		LockableValue<List<Promotion>> value = cache2.get(tenantId);
		Lock lock = value.getLock();
		try{
			lock.lock();
			LockableValue<List<Promotion>> value2 = cache2.remove(tenantId);
			if(value2 != value){
				//abnormal
			}
		}finally{
			lock.unlock();
		}
	}
	
	private void refresh(Integer tenantId, LockableValue<List<Promotion>> value) {

		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("tenantId", tenantId);
		List<Promotion> promotions = super.list("", parameters);

		List<Promotion> newList;
		if(promotions != null && promotions.size() > 0){
			newList = new ArrayList<Promotion>(promotions.size());
			for(Promotion promotion: promotions){
				Integer id = promotion.getId();
				Promotion oldPromotion = cache.get(id);
				if(oldPromotion != null){
					if(isChanged(promotion, oldPromotion)){
						prepare(promotion);
						newList.add(promotion);
						cache.put(tenantId, promotion);
						//change
					}else{
						newList.add(oldPromotion);
					}
				}else{
					prepare(promotion);
					newList.add(promotion);
					cache.put(tenantId, promotion);
					//load
				}
			}
			
		}else{
			newList = Collections.emptyList();
		}
		
		List<Promotion> oldList = value.getObj();
		
		for(Promotion promotion:oldList){
			Integer id = promotion.getId();
			Promotion oldPromotion = cache.get(id);
			if(oldPromotion == null){
				//removed;
			}
		}
		value.setObj(newList);
	}

	
	private void prepare(Promotion promotion){
		
	}

	private boolean isChanged(Promotion promotion, Promotion oldPromotion) {
		return !promotion.equals(oldPromotion);
	}


}
