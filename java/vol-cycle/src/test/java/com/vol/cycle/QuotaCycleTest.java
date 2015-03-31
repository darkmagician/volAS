package com.vol.cycle;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.vol.common.tenant.Tenant;
import com.vol.common.user.Quota;

/**
 * @author scott
 *
 */
public class QuotaCycleTest  extends BaseTest{

	
	@Resource(name="tenantCycleMgmt")
	TenantCycleMgmt tenantCycleMgmt;
	
	@Resource(name="quotaCycleMgmt")
	QuotaCycleMgmt quotaCycleMgmt;
	
	@Test
	public void testRenew(){
		Tenant tenant = new Tenant();
		tenant.setName("1111");
		Integer id =tenantCycleMgmt.add(tenant);
		
		tenant = tenantCycleMgmt.get(id);
		tenantCycleMgmt.renew(id);
		tenant = tenantCycleMgmt.get(id);
		
		Quota quota = new Quota();
		quota.setTenantId(id);
		quota.setUserName("12ss");
		Long quotaId = quotaCycleMgmt.add(quota);
		
		
		quota = quotaCycleMgmt.get(quotaId);
		
		log.info("quota1: {}", quota);
		
		
		quotaCycleMgmt.batchRenew(tenant);
		
		quota = quotaCycleMgmt.get(quotaId);
		
		log.info("quota2: {}", quota);
		Assert.assertEquals(tenant.getCycleEnd(), quota.getExpirationTime());
	}
	
}
