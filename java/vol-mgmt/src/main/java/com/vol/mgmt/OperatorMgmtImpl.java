/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.tenant.Operator;
import com.vol.dao.AbstractService;
import com.vol.mgmt.auth.CredentialUtil;

/**
 * @author scott
 *
 */
public class OperatorMgmtImpl extends AbstractService<Integer, Operator> {

	@Resource(name = "operatorDao")
	protected DAO<Integer, Operator> operatorDAO;
	@Resource(name = "mailservice")
	protected MailService mailService;

	protected void copyAttribute(Operator operator, Operator old) {
		old.setEmail(operator.getEmail());
		old.setPhone(operator.getPhone());
		// old.setStatus(operator.getStatus());
		old.setDescription(operator.getDescription());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Integer, Operator> getDAO() {
		return this.operatorDAO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vol.dao.AbstractService#validateToBeDelete(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validateToBeDelete(Operator old) {
		// super.validateToBeDelete(old);
	}

	public Operator getOperatorByUserName(final String userName) {
		try {
			txBeginRO();
			Map<String, Object> parameters = Collections.singletonMap("name",
					(Object) userName);
			Operator result = operatorDAO.find("operator.byName", parameters);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#add(com.vol.common.BaseEntity)
	 */
	@Override
	public Integer add(Operator obj) {
		String pass = CredentialUtil.generatePass();
		Integer id = add(obj, pass);
		if (id != null) {
			log.info("New Operator {} is created, Pass: {}", obj.getName(),
					pass);
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
		return super.add(obj);
	}

	public boolean updatePassword(final int id, final String oldpass,
			String newpass) {
		final String oldDigest = CredentialUtil.digest(oldpass);
		final String newDigest = CredentialUtil.digest(newpass);
		try {
			txBegin();
			Operator old = getDAO().get(id);
			if (old == null) {
				throw new MgmtException(ErrorCode.OPERATOR_NOT_FOUND);
			}
			String previousDigest = old.getPassword();
			if (previousDigest.equals(oldpass)
					|| previousDigest.equals(oldDigest)) {
				old.setPassword(newDigest);
				operatorDAO.update(old);
				txCommit();
				CredentialUtil.revoke(old.getName());
				return true;
			}
			throw new MgmtException(ErrorCode.INVALID_PASSWORD,
					"Changing password failed. Operator " + old.getName());

		} catch (Exception e) {
			txRollback(e);
		}

		return false;
	}

	public boolean resetPassword(final int id) {
		final String pass = CredentialUtil.generatePass();
		final String newDigest = CredentialUtil.digest(pass);

		try {
			txBegin();
			Operator old = getDAO().get(id);
			if (old != null) {
				old.setPassword(newDigest);
				operatorDAO.update(old);

				CredentialUtil.revoke(old.getName());
				log.info("Password of Operator {} is reset, Pass: {} ",
						old.getName(), pass);
				mailService.sendMailForReset(old, pass, null);
				return true;
			}
			throw new MgmtException(ErrorCode.OPERATOR_NOT_FOUND);
		} catch (Exception e) {
			txRollback(e);
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#validate(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validate(Operator obj) {
		validateNotNull(obj);
		validateNotEmpty(obj, "name", obj.getName());
		validateNotEmpty(obj, "email", obj.getEmail());
	}

}
