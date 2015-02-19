/**
 * 
 */
package com.vol.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;
import com.vol.common.mgmt.PagingResult;

/**
 * @author scott
 *
 */
public abstract class AbstractQueryService<K extends Serializable, V extends BaseEntity> extends AbstractTransactionService {

	
	
	public V get(final K id){
		V obj = this.readonlyTransaction.execute(new TransactionCallback<V>(){

			@Override
			public V doInTransaction(TransactionStatus status) {
				return getDAO().get(id);
			}});	
		return obj;
	}
	
	public List<V> list(final String queryName, final Map<String,Object> parameters){
		return this.readonlyTransaction.execute(new TransactionCallback<List<V>>(){

			@Override
			public List<V> doInTransaction(TransactionStatus status) {
				return getDAO().query(queryName, parameters);
			}});	
	}
	
	
	public PagingResult<V> listByPaging(final String queryName, final Map<String,Object> parameters, final int startPage, final int pageSize){
		return this.readonlyTransaction.execute(new TransactionCallback<PagingResult<V>>(){

			@Override
			public PagingResult<V> doInTransaction(TransactionStatus status) {
				return getDAO().queryByPage(queryName, parameters,startPage,pageSize);
			}});
	}
	
	protected abstract DAO<K,V> getDAO();
}
