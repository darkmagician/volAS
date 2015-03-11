/**
 * 
 */
package com.vol.pub;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class TestTenantService extends BaseTest{

	@Resource(name="tenantService")
	TenantService tenantSerivce;

	@Resource(name="tenantDao")
	DAO<Integer,Tenant> tenantDAO;
	
	@Resource(name="transactionManager")
	PlatformTransactionManager txManager;
	
	protected final TransactionTemplate tx = new TransactionTemplate();
	
	
	 public void  setup(){
		 tx.setTransactionManager(txManager);
		 tx.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				for(int i=1; i<100;i++){
					Tenant tenant = new Tenant();
					tenant.setName("Tenant"+i);
					tenantDAO.create(tenant);
				}
				
			}
			 
		 });

	}
	
	@Test
	public void test1() throws InterruptedException{
		setup();
		Thread.sleep(5000);
		Tenant tenant = tenantSerivce.get(1);
		Assert.assertEquals("Tenant1", tenant.getName());
	}
	
	
}
