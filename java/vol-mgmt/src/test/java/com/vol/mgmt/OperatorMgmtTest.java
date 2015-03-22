/**
 * 
 */
package com.vol.mgmt;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;

import com.vol.common.tenant.Operator;

/**
 * @author scott
 *
 */
public class OperatorMgmtTest  extends BaseTest{
	@Resource(name="operatorMgmt")
	 OperatorMgmtImpl operatorMgmt;
	
	@Test
	@NotTransactional
	public void testChangePassword(){
		Operator operator = new Operator();
		operator.setName("oo");
		operator.setEmail("aa");
		String oldpass="123";
		int id = operatorMgmt.add(operator,oldpass);
		String newpass = "123456";
	
		operatorMgmt.updatePassword(id, oldpass, newpass);
		
		Operator operator2 = operatorMgmt.get(id);
		log.info("old pass:{}, new pass:{}", operator.getPassword(),operator2.getPassword());
		
		Assert.assertFalse(operator.getPassword().equals(operator2.getPassword()));
	}
}
