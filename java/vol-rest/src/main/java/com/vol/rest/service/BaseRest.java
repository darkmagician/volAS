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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
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
	
	protected boolean securityCheck = true;
	
	
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

	public abstract OperationResult update(T obj,Integer id);	
	
	
	protected void checkPermission(Integer tenantId){
		if(!securityCheck){
			return;
		}
		Operator operator = getCurrentOperator();
		if(operator.getTenantId() == 0 || (tenantId!=null && tenantId == operator.getTenantId()) ){
			return;
		}
		throw new MgmtException(ErrorCode.PERMISSION_DENIED);
	}
	
	
	protected Operator getCurrentOperator(){
		if(!securityCheck){
			return new Operator();
		}
		Operator operator = null;
		HttpSession session = httppServletRequest.getSession();
		if(session != null){
			operator = (Operator) session.getAttribute("operator");
		}
		if(operator == null){
			throw new MgmtException(ErrorCode.OPERATOR_NOT_IDENTIFIED);
		}
		return operator;
	}
    

	@POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult add(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
	    	try {
				T obj = createObject();
				Map<String,String> formMap = new HashMap<String,String>();
				MapConverter.convert(map, formMap);
				BeanUtils.populate(obj, formMap);
				return create(obj);
			}  catch (MgmtException me) {
				log.error("Operation Error",me);
				OperationResult result = new OperationResult();
				result.setErrorCode(me.getCode());
				return result;
			} catch (Exception e){
				log.error("Operation Error",e);
				OperationResult result = new OperationResult();
				result.setErrorCode(ErrorCode.INTERNAL_ERROR);
				return result;
			}
    }
	
	@POST
    @Path("/update")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult update(MultivaluedMap<String,String> map){
	    	try {
				T obj = createObject();
				Map<String,String> formMap = new HashMap<String,String>();
				MapConverter.convert(map, formMap);
				BeanUtils.populate(obj, formMap);
				Integer id = getPrimaryKey(map);
				return update(obj,id);
			} catch (MgmtException me) {
				log.error("Operation Error",me);
				OperationResult result = new OperationResult();
				result.setErrorCode(me.getCode());
				return result;
			} catch (Exception e){
				log.error("Operation Error",e);
				OperationResult result = new OperationResult();
				result.setErrorCode(ErrorCode.INTERNAL_ERROR);
				return result;
			}
    }
	
	@POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public OperationResult delete(MultivaluedMap<String,String> map) throws IllegalAccessException, InvocationTargetException{
			try {
				Integer id = getPrimaryKey(map);
				return delete(id);
			} catch (MgmtException me) {
				log.error("Operation Error",me);
				OperationResult result = new OperationResult();
				result.setErrorCode(me.getCode());
				return result;
			} catch (Exception e){
				log.error("Operation Error",e);
				OperationResult result = new OperationResult();
				result.setErrorCode(ErrorCode.INTERNAL_ERROR);
				return result;
			}
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
	
	public OperationResult delete(Integer id){
		OperationResult result = new OperationResult();
		result.setErrorCode(ErrorCode.UNKNOWN_OPERATION);
		return result;
	}
	
	
	
}
