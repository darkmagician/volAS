package com.vol.schedule;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.user.Quota;
import com.vol.common.user.QuotaHistory;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class QuotaCycleMgmt extends AbstractService<Long,QuotaHistory>{
	
	
	@Resource(name="quotaDao")
	protected DAO<Long,Quota> quotaDao;
	
	@Resource(name="quotaHistoryDao")
	protected DAO<Long,QuotaHistory> quotaHisDao;
	
	@Override
	protected DAO<Long, QuotaHistory> getDAO() {
		return quotaHisDao;
	}
	
	public void execute(){
		
	}
	

}
