/**
 * 
 */
package com.vol.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;
import com.vol.common.mgmt.PagingResult;

/**
 * @author scott
 *
 */
public abstract class AbstractQueryService<K extends Serializable, V extends BaseEntity> extends AbstractTransactionService {

	
	
	public V get(final K id){
		try{
			txBeginRO();
			V obj= getDAO().get(id);
			txCommit();
			return obj;
		}catch(Exception e){
			txRollback(e);
			return null;
		}
	}
	
	public List<V> list(final String queryName, final Map<String,Object> parameters){
		try{
			txBeginRO();
			List<V> result = getDAO().query(queryName, parameters);
			txCommit();
			return result;
		}catch(Exception e){
			txRollback(e);
			return null;
		}
				
	}
	
	
	public PagingResult<V> listByPaging(final String queryName, final Map<String,Object> parameters, final int startPage, final int pageSize){
		return this.listByPaging(queryName, parameters, startPage, pageSize, null);

	}
	
	
	public PagingResult<V> listByPaging(final String queryName, final Map<String,Object> parameters, final int startPage, final int pageSize, final PostProcess<V> postProcess){
		try{
			txBeginRO();
			PagingResult<V> result = getDAO().queryByPage(queryName, parameters,startPage,pageSize);
			txCommit();
			if(postProcess != null){
				postProcess.process(result);
			}
			return result;
		}catch(Exception e){
			txRollback(e);
			return null;
		}
				
	}
	
	protected abstract DAO<K,V> getDAO();
	
	protected interface PostProcess<V>{
		public void process(PagingResult<V> result);
	}
}
