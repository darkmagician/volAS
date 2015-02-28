/**
 * 
 */
package com.vol.mgmt;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class TenantMgmtImpl extends AbstractService<Integer,Tenant>{

	@Resource(name="tenantDao")
	protected DAO<Integer,Tenant> tenantDAO;
	

	
	
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#copyAttribute(com.vol.common.BaseEntity, com.vol.common.BaseEntity)
	 */
	protected void copyAttribute(Tenant tenant, Tenant old) {
		old.setName(tenant.getName());
		old.setDescription(tenant.getDescription());
		old.setCycleType(tenant.getCycleType());
		old.setStatus(tenant.getStatus());
		
	}

	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Integer,Tenant> getDAO() {
		return this.tenantDAO;
	}


	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#validate(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validate(Tenant obj) {
		validateNotNull(obj);
		validateNotEmpty(obj, "name" , obj.getName());
	}
	

}
