/**
 * 
 */
package com.vol.mgmt.auth;

import java.util.Map;

import javax.annotation.Resource;

import com.vol.auth.AuthenticationService;
import com.vol.auth.AuthenticationServiceHolder;
import com.vol.common.tenant.Operator;
import com.vol.mgmt.OperatorMgmtImpl;

/**
 * @author scott
 *
 */
public class Authentication implements AuthenticationService {
	/**
	 * 
	 */
	private static final String DEFAULT_PASS = "superpass";
	/**
	 * 
	 */
	private static final String DEFAULT_ADMIN = "superadmin";
	/**
	 * 
	 */
	public static final String KEY = "operator";
	@Resource(name="operatorMgmt")
	private OperatorMgmtImpl operatorMgmt;
	
	
	
	public void init(){
		AuthenticationServiceHolder.getInstance().setService(this);
	}
	
	public void destroy(){
		AuthenticationServiceHolder.getInstance().unsetService(this);
	}
	

	/* (non-Javadoc)
	 * @see com.vol.auth.AuthenticationService#getCredential(java.lang.String, java.util.Map)
	 */
	@Override
	public String getCredential(String userName, Map<String, Object> context) {
		Operator operator = (Operator) context.get(KEY);
		if(operator == null){
			operator = locateOperator(operatorMgmt,userName);
			if(operator == null){
				return null;
			}
			context.put(KEY, operator);
		}
		return operator.getPassword();
	}

	public static Operator locateOperator(OperatorMgmtImpl operatorMgmt, String userName) {
		Operator operator;
		operator = operatorMgmt.getOperatorByUserName(userName);
		if(operator == null){
			if(DEFAULT_ADMIN.equals(userName)){
				operator = new Operator();
				operator.setName(userName);
				operator.setPassword(DEFAULT_PASS);
			}else{
				return null;
			}
		}
		return operator;
	}

	/* (non-Javadoc)
	 * @see com.vol.auth.AuthenticationService#getRoles(java.lang.String, java.util.Map)
	 */
	@Override
	public String[] getRoles(String userName, Map<String, Object> context) {
		Operator operator = (Operator) context.get(KEY);
		if(operator == null){
			operator = operatorMgmt.getOperatorByUserName(userName);
			if(operator == null){
				return null;
			}
			context.put(KEY, operator);
		}
		int tenantId = operator.getId();
		return new String[]{tenantId==0? "superAdmin":"admin"} ;
	}

}
