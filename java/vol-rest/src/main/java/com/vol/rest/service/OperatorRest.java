/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.mgmt.PagingResult;
import com.vol.common.tenant.Operator;
import com.vol.mgmt.OperatorMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;
import com.vol.rest.service.MapConverter.Converter;

/**
 * @author scott
 *
 */
@Path("/operator")
public class OperatorRest extends BaseRest<Operator>{
	

	@Resource(name="operatorMgmt")
	private OperatorMgmtImpl operatorMgmt;
	
    @GET
    @Path("/{operatorId}")
    @Produces("application/json")
	public Operator get(@PathParam("operatorId")Integer operatorId){
    	try {
			checkPermission(null);
			Operator operator = operatorMgmt.get(operatorId);
			operator.setPassword(null);
			return operator;
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }
    
    @POST
    @Path("/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Operator> listPage(@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
    	try {
			checkPermission(null);
			return operatorMgmt.listByPaging("operator.all", Collections.<String, Object> emptyMap(), startPage, pageSize);
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }
    
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Operator> list(@Context UriInfo uriInfo){
    	try {
			checkPermission(null);
			MultivaluedMap<String, String> pathPara = uriInfo.getQueryParameters();
			String queryName = pathPara.getFirst("query");
			
			if(queryName == null || "".equals(queryName)){
				return operatorMgmt.list("operator.all", Collections.<String, Object> emptyMap());
			}else{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("current", System.currentTimeMillis());
				MapConverter.convert(pathPara, map, Collections.<String, Converter> emptyMap());
				return operatorMgmt.list("operator."+queryName, map);
				
			}
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }  
    
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public PutOperationResult create(Operator operator){
    	checkPermission(null);
    	PutOperationResult result = new PutOperationResult();
		Integer id = operatorMgmt.add(operator);
		result.setErrorCode(ErrorCode.SUCCESS);
		result.setId(id.intValue());
		return result;
    }
    
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public OperationResult update(Operator operator, @PathParam("id")Integer id){
    	checkPermission(null);
    	OperationResult result = new OperationResult();
		operator.setId(id);
		operatorMgmt.update(id,operator);
		result.setErrorCode(ErrorCode.SUCCESS);
		return result;
    }

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#createObject()
	 */
	@Override
	public Operator createObject() {
		return new Operator();
	}
	
	
	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#delete(java.lang.Integer)
	 */
	@Override
	public OperationResult delete(Integer id) {
		checkPermission(null);
		OperationResult result = new OperationResult();
		operatorMgmt.delete(id);
		result.setErrorCode(ErrorCode.SUCCESS);
		return result;
	}
	
    @POST
    @Path("/changepass")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public OperationResult changePassword(@FormParam("oldpass")String oldpass,@FormParam("newpass")String newpass){
    	
    	try {
    		OperationResult result = new OperationResult();
			Operator operator = getCurrentOperator();
			operatorMgmt.updatePassword(operator.getId(), oldpass, newpass);
			result.setErrorCode(ErrorCode.SUCCESS);
			return result;
		}catch (MgmtException me) {
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
    
}
