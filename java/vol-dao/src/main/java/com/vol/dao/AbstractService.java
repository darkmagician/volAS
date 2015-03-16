/**
 * 
 */
package com.vol.dao;

import java.io.Serializable;
import java.util.Date;

import com.vol.common.BaseEntity;
import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;

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
			throw new MgmtException(ErrorCode.CONCURRENT_MODIFICATION,"Entity "+newEntity.getClass()+" is updated since "+ new Date(newEntity.getUpdateTime()));
		}
	}



	
	public K add(final V obj){
		validate(obj);
		try{
			txBegin();
			initEntity(obj);
			K id = getDAO().create(obj);
			txCommit();
			return id;
		}catch(Exception e){
			txRollback(e);
			return null;
		}
	}
	
	
	protected void validate(V obj) {
	
		
	}




	public void update(final K id, final V obj){
		validate(obj);
		try{
			txBegin();
			V old = getDAO().get(id);
			validateUpdateTime(obj, old);
			updateEntity(old);
			copyAttribute(obj,old);
			getDAO().update(old);
			txCommit();
		}catch(Exception e){
			txRollback(e);
		}
	}
	protected  void copyAttribute(V obj, V old){};
	
	
	public void delete(final K id){
		try{
			txBegin();
			V old = getDAO().get(id);
			if(old == null){
				throw new MgmtException(ErrorCode.ENTITY_NOT_EXIST,"To be Enity "+id+" doesnt exist. ");
			}
			validateToBeDelete(old);
			getDAO().delete(old);
			txCommit();
		}catch(Exception e){
			txRollback(e);
		}
	}
	

	protected void validateToBeDelete(V old) {
		throw new MgmtException(ErrorCode.OPERATION_NOT_SUPPORT,"DELETE is not supported");
	}
	
	protected void validateNotEmpty(Object obj, String field, String value){
		if(value == null || "".equals(value.trim())){
			throw new MgmtException(ErrorCode.EMPTY_FIELD,"Field "+field+" of "+obj.getClass()+" should NOT be empty");
		}
	}
	protected void validateNotNull(Object obj){
		if(obj == null){
			throw new MgmtException(ErrorCode.NULL_OBJECT,"obj should NOT be null");
		}
	}
	
	protected void validateDate(Object obj,String field, long value){
		if(value <= 0){
			throw new MgmtException(ErrorCode.EMPTY_DATE_FILED,"Field "+field+" of "+obj.getClass()+" should be a valid datetime");
		}
	}
}
