/**
 * 
 */
package com.vol.rest.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.tenant.Operator;
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
	
	@Context
	protected HttpServletRequest httppServletRequest;
	
	
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
	public abstract PutOperationResult create(T obj);
	
	/**
	 * Update.
	 *
	 * @param obj
	 *            the obj
	 * @return the operation result
	 */

	public abstract OperationResult update(T obj, @PathParam("id")Integer id);	
	
	
	protected void checkPermission(Integer tenantId){
		Operator operator = getCurrentOperator();
		if(operator != null){
			if(operator.getTenantId() == 0 || (tenantId!=null && tenantId == operator.getTenantId()) ){
				//if(log.isTraceEnabled()){
					log.info("Permission is allowed for operator:{} accessing tenant {}", operator.getName(),tenantId);
			//	}
				return;
			}
			
		}else{
			log.warn("operator is mssing0");
		}
		log.warn("Permission NOT is allowed for accessing tenant {}", tenantId);
	}
	
	protected Operator getCurrentOperator(){
		HttpSession session = httppServletRequest.getSession();
		if(session != null){
			return (Operator) session.getAttribute("operator");
		}
		return null;
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
	
	public OperationResult delete(Integer id){
		OperationResult result = new OperationResult();
		result.setCode(OperationResult.UNKNOWN_ACTION);
		return result;
	}
	
	
	
}
