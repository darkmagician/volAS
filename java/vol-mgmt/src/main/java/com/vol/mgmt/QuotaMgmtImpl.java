/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.DAO;
import com.vol.common.mgmt.PagingResult;
import com.vol.common.user.Quota;
import com.vol.common.user.User;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public class QuotaMgmtImpl  extends AbstractQueryService<Long,Quota>  {
	@Resource(name="quotaDao")
	protected DAO<Long,Quota> quotaDao;
	@Resource(name="userDao")
	protected DAO<Long,User> userDao;
	
	public List<Quota> getQuotasByUser( final Long userid){
		List<Quota> list = this.readonlyTransaction
				.execute(new TransactionCallback<List<Quota>>() {

					@Override
					public List<Quota> doInTransaction(TransactionStatus status) {
						Map<String, Object> parameters = Collections.singletonMap("userId", (Object)userid);
						return quotaDao.query("quota.byUser", parameters);
					}
				});
		
		 adjustBalance(list);
		return list;
	}
	public List<Quota> getQuotasByUserName(final Integer tenantid, final String userName){
		List<Quota> list = this.readonlyTransaction.execute(new TransactionCallback<List<Quota>>(){

			@Override
			public List<Quota> doInTransaction(TransactionStatus status) {
				Map<String,Object> parameters= new HashMap<String,Object> ();
				parameters.put("userName", userName);
				parameters.put("tenantId", tenantid);
				return quotaDao.query("quota.byUserName", parameters);
			}});	
		 adjustBalance(list);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractQueryService#getDAO()
	 */
	@Override
	protected DAO<Long, Quota> getDAO() {
		return quotaDao;
	}
	
	private void adjustBalance(List<Quota> quotas){
		long current = System.currentTimeMillis();
		for(Quota quota: quotas){
			adjustBalance(quota,  current);
		}
	}
	private void adjustBalance(Quota quota, long current) {
		if(quota.getExpirationTime() < current){
			quota.setMaximum(0);
			quota.setBalance(0);
		}
		
	}
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractQueryService#get(java.io.Serializable)
	 */
	@Override
	public Quota get(Long id) {
		Quota q = super.get(id);
		adjustBalance(q, System.currentTimeMillis());
		return q;
	}
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractQueryService#list(java.lang.String, java.util.Map)
	 */
	@Override
	public List<Quota> list(String queryName, Map<String, Object> parameters) {
		 List<Quota> list = super.list(queryName, parameters);
		 adjustBalance(list);
		 return list;
	}
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractQueryService#listByPaging(java.lang.String, java.util.Map, int, int)
	 */
	@Override
	public PagingResult<Quota> listByPaging(String queryName,
			Map<String, Object> parameters, int startPage, int pageSize) {
		PagingResult<Quota> result = super.listByPaging(queryName, parameters, startPage, pageSize);
		 adjustBalance(result.getRows());
		 return result;
	}
}
