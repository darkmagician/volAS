package com.vol.pub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Promotion;

public class PromotionService  extends AbstractCache<Integer,Promotion>  {
	
	@Resource(name="promotionDao")
	protected DAO<Integer,Promotion> promotionDAO;
	
	protected final ConcurrentMap<Integer,Promotion> cache = new ConcurrentHashMap<Integer,Promotion>(); 
	
	protected final ConcurrentMap<Integer,LockableValue<List<Promotion>>> cache2 = new ConcurrentHashMap<Integer,LockableValue<List<Promotion>>>(); 
	
	
	@Override
	protected DAO<Integer, Promotion> getDAO() {
		return promotionDAO;
	}
	
	
	@Override
	public Promotion get(Integer id) {
		return cache.get(id);
	}
	
	
	public List<Promotion> getByTenant(Integer tenantId){
		LockableValue<List<Promotion>> lv = cache2.get(tenantId);
		return lv == null? null : lv.getObj();
	}
	
	
	public void sync(){
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
						onChange(id, oldPromotion, promotion);
					}else{
						newList.add(oldPromotion);
					}
				}else{
					prepare(promotion);
					newList.add(promotion);
					cache.put(tenantId, promotion);
					onLoad(id, promotion);
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
				onUnload(id, promotion);
			}
		}
		value.setObj(newList);
	}

	
	private void prepare(Promotion promotion){
		
	}



}
