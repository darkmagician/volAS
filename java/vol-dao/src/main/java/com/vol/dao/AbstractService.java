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
import com.vol.common.mgmt.VolMgmtException;

/**
 * @author scott
 *
 */
public class AbstractService {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource(name="transactionManager")
	protected PlatformTransactionManager txManager;
	
	protected final TransactionTemplate readonlyTransaction = new TransactionTemplate();
	protected final TransactionTemplate transaction = new TransactionTemplate();
	
	public void init(){ 
		log.info("{} is starting.",getClass());
		readonlyTransaction.setReadOnly(true);
		readonlyTransaction.setTransactionManager(txManager);
		transaction.setTransactionManager(txManager);
	}
	
	/**
	 * @param newEntity
	 * @param oldEntity
	 */
	protected void validateUpdateTime(final BaseEntity newEntity,
			BaseEntity oldEntity) {
		if(oldEntity.getUpdateTime() != newEntity.getUpdateTime()){
			log.error("Entity {} is updated since {}",newEntity.getClass(), newEntity.getUpdateTime());
			throw new VolMgmtException("The record is modifed since last load. Please refresh and try again");
		}
	}

	/**
	 * @param obj
	 */
	public static void initEntity(BaseEntity obj) {
		obj.setStatus(BaseEntity.ACTIVE);
		long now = System.currentTimeMillis();
		obj.setUpdateTime(now);
		obj.setCreationTime(now);
	}
	
	/**
	 * @param obj
	 */
	static public void updateEntity(BaseEntity obj) {
		long now = System.currentTimeMillis();
		obj.setUpdateTime(now);
	}
}
