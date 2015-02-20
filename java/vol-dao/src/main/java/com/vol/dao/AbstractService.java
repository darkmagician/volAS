/**
 * 
 */
package com.vol.dao;

import java.io.Serializable;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.vol.common.BaseEntity;
import com.vol.common.mgmt.VolMgmtException;

/**
 * @author scott
 *
 */
public abstract class AbstractService<K extends Serializable, V extends BaseEntity> extends AbstractQueryService<K,V>{

	

	
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



	
	public K add(final V obj){
		K id = this.transaction.execute(new TransactionCallback<K>(){

			@Override
			public K doInTransaction(TransactionStatus status) {
				initEntity(obj);
				return getDAO().create(obj);
			}

			});
		return id;
	}
	
	
	public void update(final K id, final V obj){
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				V old = getDAO().get(id);
				validateUpdateTime(obj, old);
				updateEntity(old);
				copyAttribute(obj,old);
				getDAO().update(old);
				
			}

		});
	}
	protected  void copyAttribute(V obj, V old){};
	
	
	public void delete(final K id){
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				V old = getDAO().get(id);
				if(old == null){
					throw new IllegalStateException("The object doesnt exist.");
				}
				validateToBeDelete(old);
				getDAO().delete(old);
				
			}


		});
	}
	

	protected void validateToBeDelete(V old) {
		throw new UnsupportedOperationException("DELETE is not supported");
	}
}
