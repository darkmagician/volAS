/**
 * 
 */
package com.vol.dao;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import com.vol.common.BaseEntity;

/**
 * @author scott
 *
 */
public class AbstractTransactionService {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	private static ThreadLocal<TransactionStatus> txHolder = new ThreadLocal<TransactionStatus>();
	
	@Resource(name="transactionManager")
	private PlatformTransactionManager txManager;
	
	private final TransactionTemplate readonlyTransaction = new TransactionTemplate();
	private final TransactionTemplate transaction = new TransactionTemplate();
	private final short init_state = getInitState();
	
	
	protected void txBegin(){
		TransactionStatus tx = txManager.getTransaction(transaction);
		txHolder.set(tx);
	}
	protected void txBeginRO(){
		TransactionStatus tx = txManager.getTransaction(readonlyTransaction);
		txHolder.set(tx);
	}
	
	protected void txRollback(Exception e){
		TransactionStatus tx = txHolder.get();
		if(tx != null){
			txHolder.remove();
			txManager.rollback(tx);
		}
		if(e instanceof RuntimeException){
			throw (RuntimeException)e;
		}
		
	}
	
	protected void txRollback(){
		TransactionStatus tx = txHolder.get();
		if(tx != null){
			txHolder.remove();
			txManager.rollback(tx);
		}
	}
	
	protected void txCommit(){
		TransactionStatus tx = txHolder.get();
		if(tx != null){
			txHolder.remove();
			txManager.commit(tx);

		}
	}
	
	public void init(){ 
		log.info("{} is starting.",getClass());
		readonlyTransaction.setReadOnly(true);
		readonlyTransaction.setTransactionManager(txManager);
		transaction.setTransactionManager(txManager);
	}
	public void destroy(){ 
		log.info("{} is stopping.",getClass());
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
