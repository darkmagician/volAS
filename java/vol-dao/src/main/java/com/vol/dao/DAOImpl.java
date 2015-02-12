/**
 * 
 */
package com.vol.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;

/**
 * The Class DAOImpl.
 *
 * @author scott
 * @param <K>
 *            the key type
 * @param <T>
 *            the generic type
 */
public class DAOImpl<K extends Serializable, T extends BaseEntity> implements DAO<K , T>{

	/**
	 * The session factory.
	 */
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * The clazz.
	 */
	final private Class<T> clazz;

	/**
	 * Instantiates a new abstract dao.
	 *
	 * @param clazz
	 *            the clazz
	 */
	public DAOImpl(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	/**
	 * Gets the current session.
	 *
	 * @return the current session
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Creates the.
	 *
	 * @param obj
	 *            the obj
	 * @return the k
	 */
	@Override
	public K create(T obj) {
		Session session = getCurrentSession();
		@SuppressWarnings("unchecked")
		K id = (K) session.save(obj);
		return id;
	}


	/**
	 * Gets the.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T get(K id) {
		Session session = getCurrentSession();
		return (T) session.get(clazz, id);
	}

	/**
	 * Update.
	 *
	 * @param obj
	 *            the obj
	 */
	@Override
	public void update(T obj) {
		Session session = getCurrentSession();
		session.update(obj);
	}


	public void delete(T obj){
		Session session = getCurrentSession();
		session.delete(obj);
	}
	
	/**
	 * Query.
	 *
	 * @param queryName
	 *            the query name
	 * @param parameters
	 *            the parameters
	 * @return the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> query(String queryName, Map<String, Object> parameters ){
		Session session = getCurrentSession();
		Query query=session.getNamedQuery(queryName);
		query.setProperties(parameters);
		return query.list();
	}
	
	/**
	 * Batch update.
	 *
	 * @param queryName
	 *            the query name
	 * @param parameters
	 *            the parameters
	 * @return the int
	 */
	@Override
	public int batchUpdate(String queryName, Map<String, Object> parameters ){
		Session session = getCurrentSession();
		Query query=session.getNamedQuery(queryName);
		query.setProperties(parameters);
		return query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.vol.common.DAO#find(java.lang.String, java.util.Map)
	 */
	@Override
	public T find(String queryName, Map<String, Object> parameters) {
		List<T> list = query(queryName, parameters);
		return list==null || list.isEmpty()? null: list.iterator().next();
	}
}
