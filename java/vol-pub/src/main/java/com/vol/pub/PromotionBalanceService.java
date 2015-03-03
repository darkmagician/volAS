package com.vol.pub;

import com.vol.common.DAO;
import com.vol.common.tenant.PromotionBalance;
import com.vol.dao.AbstractQueryService;

public class PromotionBalanceService extends AbstractQueryService<Integer,PromotionBalance>{
	
	private String hostId;
	private final static long startup=System.currentTimeMillis();

	@Override
	protected DAO<Integer, PromotionBalance> getDAO() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public boolean flush(Integer promotionId, boolean isFinal){
		return false;
	}
	
	public boolean reserve(Integer promotionId){
		
		return false;
	}

}
