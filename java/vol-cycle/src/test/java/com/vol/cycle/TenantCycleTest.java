/**
 * 
 */
package com.vol.cycle;

import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class TenantCycleTest extends BaseTest {

	@Resource(name="tenantCycleMgmt")
	TenantCycleMgmt tenantCycleMgmt;
	
	@Test
	public void testRenew(){
		Tenant tenant = new Tenant();
		tenant.setName("1111");
		Integer id =tenantCycleMgmt.add(tenant);
		
		tenant = tenantCycleMgmt.get(id);
		
		log.info("T1: {}",tenant);
		
		tenantCycleMgmt.renew(id);
		tenant = tenantCycleMgmt.get(id);
		
		log.info("T2: {}",tenant);
		
		log.info("new Date: {}",new Date(tenant.getCycleEnd()));
		
		Assert.assertTrue(tenant.getCycleEnd()>0);
		Assert.assertTrue(tenant.getCycleStart()==0);
		
	}
}
