/**
 * 
 */
package com.vol.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * The Interface DAO.
 *
 * @author scott
 * @param <K>
 *            the key type
 * @param <T>
 *            the generic type
 */
public interface DAO <K extends Serializable, T extends BaseEntity>{
	/**
	 * Creates the.
	 *
	 * @param obj
	 *            the obj
	 * @return the k
	 */
	public K create(T obj) ;

	/**
	 * Batch update.
	 *
	 * @param queryName
	 *            the query name
	 * @param parameters
	 *            the parameters
	 * @return the int
	 */
	public abstract int batchUpdate(String queryName, Map<String, Object> parameters);

	/**
	 * Query.
	 *
	 * @param queryName
	 *            the query name
	 * @param parameters
	 *            the parameters
	 * @return the list
	 */
	public abstract List<T> query(String queryName, Map<String, Object> parameters);
	
	
	
	/**
	 * Find.
	 *
	 * @param queryName
	 *            the query name
	 * @param parameters
	 *            the parameters
	 * @return the t
	 */
	public abstract T find(String queryName, Map<String, Object> parameters);

	/**
	 * Update.
	 *
	 * @param obj
	 *            the obj
	 */
	public abstract void update(T obj);

	/**
	 * Gets the.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 */
	public abstract T get(K id);
}