/**
 * 
 */
package com.vol.mgmt;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Operator;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class OperatorMgmtImpl extends AbstractService<Integer,Operator>{

	@Resource(name="operatorDao")
	protected DAO<Integer,Operator> operatorDAO;
	
	

	protected void copyAttribute(Operator operator, Operator old) {
		old.setName(operator.getName());
		old.setStatus(operator.getStatus());
		
	}
	
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Integer,Operator> getDAO() {
		return this.operatorDAO;
	}
	

}
