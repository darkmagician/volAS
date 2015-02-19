/**
 * 
 */
package com.vol.dao;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.vol.common.BaseEntity;

/**
 * @author scott
 *
 */
public class AbstractTransactionService {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	
	@Resource(name="transactionManager")
	protected PlatformTransactionManager txManager;
	
	protected final TransactionTemplate readonlyTransaction = new TransactionTemplate();
	protected final TransactionTemplate transaction = new TransactionTemplate();
	private final short init_state = getInitState();
	
	public void init(){ 
		log.info("{} is starting.",getClass());
		readonlyTransaction.setReadOnly(true);
		readonlyTransaction.setTransactionManager(txManager);
		transaction.setTransactionManager(txManager);
	}
	
	
	protected short getInitState(){
		return BaseEntity.ACTIVE;
	}
	/**
	 * @param obj
	 */
	public void initEntity(BaseEntity obj) {
		obj.setStatus(init_state);
		long now = System.currentTimeMillis();
		obj.setUpdateTime(now);
		obj.setCreationTime(now);
	}
	
	/**
	 * @param obj
	 */
	public void updateEntity(BaseEntity obj) {
		long now = System.currentTimeMillis();
		obj.setUpdateTime(now);
	}
		
}
