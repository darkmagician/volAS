/**
 * 
 */
package com.vol.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.util.StringParser;
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
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
	public abstract OperationResult update(T obj, @PathParam("id")Integer id);	
    
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
    	Map<String,String> formMap = new HashMap<String,String>();
    	MapConverter.convert(map, formMap);
		BeanUtils.populate(obj, formMap);
		return create(obj);
    	
    }

	@POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult add(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
	    	T obj = createObject();
	    	Map<String,String> formMap = new HashMap<String,String>();
	    	MapConverter.convert(map, formMap);
			BeanUtils.populate(obj, formMap);
			return create(obj);
    }
	
	@POST
    @Path("/update")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult update(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
	    	T obj = createObject();
	    	Map<String,String> formMap = new HashMap<String,String>();
	    	MapConverter.convert(map, formMap);
			BeanUtils.populate(obj, formMap);
			Integer id = getPrimaryKey(map);
			return update(obj,id);
    }
	
	@POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult delete(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
			Integer id = getPrimaryKey(map);
			return delete(id);
    }

	/**
	 * @param map
	 * @return
	 */
	private Integer getPrimaryKey(MultivaluedMap<String, String> map) {
		Integer id = StringParser.parseInteger(map.getFirst("id"));
		return id;
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
    @Path("/{id}")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult update(MultivaluedMap<String,String> map, @PathParam("id")Integer id, @QueryParam("action")String action) throws IllegalAccessException, InvocationTargetException{
		if(action != null){
	    	T obj = createObject();
	    	Map<String,String> formMap = new HashMap<String,String>();
	    	MapConverter.convert(map, formMap);
			BeanUtils.populate(obj, formMap);
			if(action.equals("add")){
				return create(obj);
			} else if(action.equals("update")){
				return update(obj,id);
			} else if(action.equals("delete")){
				return delete(id);
			}
		}
		OperationResult result = new OperationResult();
		result.setCode(OperationResult.UNKNOWN_ACTION);
		return result;
    }
	
	public OperationResult delete(Integer id){
		OperationResult result = new OperationResult();
		result.setCode(OperationResult.UNKNOWN_ACTION);
		return result;
	}
	
	
	
}
