/**
 * 
 */
package com.vol.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;

import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
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
public abstract class CURDRest<T> extends RestContext {

	/**
	 * Creates the object.
	 *
	 * @return the t
	 */
	protected abstract T createObject();
	
	/**
	 * Creates the.
	 *
	 * @param obj
	 *            the obj
	 * @return the put operation result
	 */
	protected abstract PutOperationResult _create(T obj);
	
	/**
	 * Update.
	 *
	 * @param obj
	 *            the obj
	 * @return the operation result
	 */

	protected abstract OperationResult _update(T obj,Integer id);	
	
	
	@POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({PRODUCER_TYPE_FOR_SUBMIT,"application/json"})
    public OperationResult add(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
				T obj = createObject();
				Map<String,String> formMap = new HashMap<String,String>();
				MapConverter.convert(map, formMap);
				BeanUtils.populate(obj, formMap);
				return _create(obj);
    }
	
	@POST
    @Path("/update")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({PRODUCER_TYPE_FOR_SUBMIT,"application/json"})
    public OperationResult update(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
				T obj = createObject();
				Map<String,String> formMap = new HashMap<String,String>();
				MapConverter.convert(map, formMap);
				BeanUtils.populate(obj, formMap);
				Integer id = getPrimaryKey(map);
				return _update(obj,id);
    }
	
	@POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({PRODUCER_TYPE_FOR_SUBMIT,"application/json"})
    public OperationResult delete(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
				Integer id = getPrimaryKey(map);
				return _delete(id);
    }

	/**
	 * @param map
	 * @return
	 */
	private Integer getPrimaryKey(MultivaluedMap<String, String> map) {
		Integer id = StringParser.parseInteger(map.getFirst("id"));
		if(id == null){
			throw new MgmtException(ErrorCode.PRIMARY_KEY_MISSING);
		}
		return id;
	}
	
	protected OperationResult _delete(Integer id){
		OperationResult result = new OperationResult();
		result.setErrorCode(ErrorCode.UNKNOWN_OPERATION);
		return result;
	}
	
	
	
}
