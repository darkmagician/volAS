/**
 * 
 */
package com.vol.mgmt;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class TenantMgmtImpl extends AbstractService{

	@Resource(name="tenantDao")
	protected DAO<Integer,Tenant> tenantDAO;
	
	public Integer addTenant(final Tenant tenant){
		validateTenant(tenant);
		Integer id = this.transaction.execute(new TransactionCallback<Integer>(){

			@Override
			public Integer doInTransaction(TransactionStatus status) {
				initEntity(tenant);
				return tenantDAO.create(tenant);
			}

			});
		if(log.isDebugEnabled()){
			log.debug("promotion id={} is added", id);
		}		
		return id;
	}
	
	private void validateTenant(Tenant tenant) {
		//if(promo)
		
	}
	
	public void updateTenant(final Tenant tenant){
		validateTenant(tenant);
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Tenant old = tenantDAO.get(tenant.getId());
				validateUpdateTime(tenant, old);
				updateEntity(tenant);
				tenantDAO.update(tenant);
				
			}


		});
	}
	
	public Tenant getTenant(final int id){
		Tenant tenant = this.readonlyTransaction.execute(new TransactionCallback<Tenant>(){

			@Override
			public Tenant doInTransaction(TransactionStatus status) {
				return tenantDAO.get(id);
			}});	
		return tenant;
	}
	
	public List<Tenant> list(final String queryName, final Map<String,Object> parameters){
		return this.readonlyTransaction.execute(new TransactionCallback<List<Tenant>>(){

			@Override
			public List<Tenant> doInTransaction(TransactionStatus status) {
				return tenantDAO.query(queryName, parameters);
			}});	
	}
}
