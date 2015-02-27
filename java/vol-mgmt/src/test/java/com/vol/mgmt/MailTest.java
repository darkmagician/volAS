package com.vol.mgmt;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;

import com.vol.common.tenant.Operator;

public class MailTest extends BaseTest{

	@Resource(name="mailservice")
	protected MailService mailService;
	
	@Test
	@NotTransactional
	public void testMail(){
		Operator operator = new Operator();
		operator.setName("KKK");
		operator.setEmail("realsuper@163.com");
		mailService.sendMailForRegistration(operator, "123456", null);
	}
	
}
