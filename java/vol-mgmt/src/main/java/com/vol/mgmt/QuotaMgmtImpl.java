/**
 * 
 */
package com.vol.mgmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.DAO;
import com.vol.common.user.Quota;
import com.vol.common.user.User;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class QuotaMgmtImpl extends AbstractService {
	@Resource(name="quotaDao")
	protected DAO<Long,Quota> quotaDao;
	@Resource(name="userDao")
	protected DAO<Long,User> userDao;
	
	public Quota getQuota(final long id){
		Quota quota = this.readonlyTransaction.execute(new TransactionCallback<Quota>(){

			@Override
			public Quota doInTransaction(TransactionStatus status) {
				return quotaDao.get(id);
			}});	
		return quota;
	}
	
	public List<Quota> getQuotasByUserName(final Integer tenantid, final String userName){
		return this.readonlyTransaction.execute(new TransactionCallback<List<Quota>>(){

			@Override
			public List<Quota> doInTransaction(TransactionStatus status) {
				Map<String,Object> parameters= new HashMap<String,Object> ();
				parameters.put("name", userName);
				parameters.put("tenantId", tenantid);
				User user = userDao.find("user.byName", parameters);
				if(user == null){
					if(log.isDebugEnabled()){
						log.debug("quota is not found because User[name={},tenantid={}] is not found",userName,tenantid);
					}
					return null;
				}	
				parameters.clear();
				parameters.put("userId", user.getId());
				return quotaDao.query("quota.byUser", parameters);
			}});	
	}
	
	public List<Quota> list(final String queryName, final Map<String,Object> parameters){
		return this.readonlyTransaction.execute(new TransactionCallback<List<Quota>>(){

			@Override
			public List<Quota> doInTransaction(TransactionStatus status) {
				return quotaDao.query(queryName, parameters);
			}});	
	}
}
