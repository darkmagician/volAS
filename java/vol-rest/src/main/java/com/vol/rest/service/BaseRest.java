/**
 * 
 */
package com.vol.rest.service;

import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;

/**
 * The Class BaseRest.
 *
 * @author scott
 * @param <T>
 *            the generic type
 */
public abstract class BaseRest<T> {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * Creates the object.
	 *
	 * @return the t
	 */
	public abstract T createObject();
	
	/**
	 * Creates the.
	 *
	 * @param obj
	 *            the obj
	 * @return the put operation result
	 */
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
	public abstract PutOperationResult create(T obj);
	
	/**
	 * Update.
	 *
	 * @param obj
	 *            the obj
	 * @return the operation result
	 */
    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
	public abstract OperationResult update(T obj);	
    
    /**
	 * Creates the.
	 *
	 * @param map
	 *            the map
	 * @return the put operation result
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
    @PUT
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public PutOperationResult create(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
    	T obj = createObject();
		BeanUtils.populate(obj, new FormMap(map));
		return create(obj);
    	
    }


	/**
	 * Update.
	 *
	 * @param map
	 *            the map
	 * @return the operation result
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
	@POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult update(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
    	T obj = createObject();
		BeanUtils.populate(obj, new FormMap(map));
		return update(obj);
    	
    }
}