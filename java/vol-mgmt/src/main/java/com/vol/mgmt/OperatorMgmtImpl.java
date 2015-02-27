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
	@Resource(name="mailservice")
	protected MailService mailService;
	
	

	protected void copyAttribute(Operator operator, Operator old) {
		old.setEmail(operator.getEmail());
		old.setPhone(operator.getPhone());
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
		Integer id = add(obj, pass);
		if(id != null){
			mailService.sendMailForRegistration(obj, pass, null);
		}
		return id;
	}

	/**
	 * @param obj
	 * @param pass
	 * @return
	 */
	public Integer add(Operator obj, String pass) {
		obj.setPassword(CredentialUtil.digest(pass));
		log.info("New User name:{}, pass:{}, digest:{}", obj.getName(), pass, obj.getPassword());
		return super.add(obj);
	}
	
	
	public boolean updatePassword(final int id, final String oldpass, String newpass){
		final String oldDigest = CredentialUtil.digest(oldpass);
		final String newDigest = CredentialUtil.digest(newpass);
		boolean rc= this.transaction.execute(new TransactionCallback<Boolean>(){

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				Operator old = getDAO().get(id);
				String previousDigest = old.getPassword();
				if(previousDigest.equals(oldDigest)){
					old.setPassword(newDigest);
					operatorDAO.update(old);
					CredentialUtil.revoke(old.getName());
					return true;
				}
				log.info("old pass {}, old degist {}, pre digest {}", oldpass, oldDigest, previousDigest);
				return false;
			}

		});
		return rc;
	}
	
	public boolean resetPassword(final int id){
		final String pass = CredentialUtil.generatePass();
		Operator operator =  this.transaction.execute(new TransactionCallback<Operator>(){

			@Override
			public Operator doInTransaction(TransactionStatus status) {
				Operator old = getDAO().get(id);
				if(old != null){
					old.setPassword(pass);
					operatorDAO.update(old);
					return old;
				}
				return null;
			}

		});
		if(operator != null )
		{
			mailService.sendMailForRegistration(operator, pass, null);
			return true;
		}
		return false;
	}
	
	
}
