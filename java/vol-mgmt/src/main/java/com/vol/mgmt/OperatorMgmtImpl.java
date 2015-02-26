/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.DAO;
import com.vol.common.tenant.Operator;
import com.vol.dao.AbstractService;
import com.vol.mgmt.auth.CredentialUtil;

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
		old.setDescription(operator.getDescription());
		
	}
	
	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Integer,Operator> getDAO() {
		return this.operatorDAO;
	}
	

	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#validateToBeDelete(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validateToBeDelete(Operator old) {
		//super.validateToBeDelete(old);
	}
	
	
	public Operator getOperatorByUserName( final String userName){
		return this.readonlyTransaction.execute(new TransactionCallback<Operator>(){

			@Override
			public Operator doInTransaction(TransactionStatus status) {
				Map<String,Object> parameters= Collections.singletonMap("name", (Object)userName);
				Operator operator = operatorDAO.find("operator.byName", parameters);
				return operator;

			}});	
	}

	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#add(com.vol.common.BaseEntity)
	 */
	@Override
	public Integer add(Operator obj) {
		String pass = CredentialUtil.generatePass();
		obj.setPassword(CredentialUtil.digest(pass));
		log.info("New User name:{}, pass:{}, digest:{}", obj.getName(), pass, obj.getPassword());
		return super.add(obj);
	}
}
