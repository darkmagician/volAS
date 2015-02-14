/**
 * 
 */
package com.vol.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import com.vol.common.DAO;
import com.vol.common.user.User;


/**
 * @author scott
 *
 */
public class UserDAOTest extends BaseTest{

	@Resource
	DAO<Long,User> userDao;
	@Autowired
	private  PlatformTransactionManager txManager;
	
	
	@Test
	@NotTransactional 
	public void testCreateUser(){
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus txStatus = txManager.getTransaction(definition);
		
		User user = new User();
		user.setName("111");
		user.setTenantId(1);
		Long id = userDao.create(user );
		Assert.notNull(id);
		System.out.println("ddd"+id);
		
		Map<String, Object> parameters = new HashMap();
		parameters.put("name","111");
		parameters.put("tenantId",1);
		List<User> users = userDao.query("user.byName", parameters);
		System.out.println("ddd"+users.get(0));
		
		txManager.commit(txStatus);
		
	}
}
