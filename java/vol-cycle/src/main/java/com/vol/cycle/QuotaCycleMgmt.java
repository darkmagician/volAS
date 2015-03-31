package com.vol.cycle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.common.user.Quota;
import com.vol.common.user.QuotaHistory;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class QuotaCycleMgmt extends AbstractService<Long,Quota>{
	
	
	@Resource(name="quotaDao")
	protected DAO<Long,Quota> quotaDao;
	
	@Resource(name="quotaHistoryDao")
	protected DAO<Long,QuotaHistory> quotaHisDao;
	
	@Override
	protected DAO<Long, Quota> getDAO() {
		return quotaDao;
	}
	
	public boolean renew(Quota quota, Tenant tenant, Long current){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", quota.getId());
		map.put("newExpirationTime", tenant.getCycleEnd());
		map.put("updateTime", current);
		map.put("oldExpirationTime", quota.getExpirationTime());
		map.put("initial", 100L);
		int rc = quotaDao.batchUpdate("quota.renew", map);
		if(rc==1){
			quotaHisDao.create(new QuotaHistory(quota));
		}
		return rc == 1;
	}
	

	
	public int batchRenew(Tenant tenant){
		int renewNum=0;
		final int batchSize = 300;
		final Long current = System.currentTimeMillis();
		try {
			txBegin();
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("current", current);
			parameters.put("tenantId", tenant.getId());
			List<Quota> result = quotaDao.query("quota.expired", parameters,batchSize);
			for(Quota quota: result){
				boolean success = renew(quota,tenant, current);
				if(success){
					renewNum++;
				}
			}
			txCommit();
			return renewNum;
		} catch (Exception e) {
			txRollback(e);
		}
		return -1;
	}
}
