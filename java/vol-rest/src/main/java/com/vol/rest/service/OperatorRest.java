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
import com.vol.common.tenant.Operator;
import com.vol.mgmt.OperatorMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PagingOperationResult;
import com.vol.rest.result.PutOperationResult;
import com.vol.rest.service.MapConverter.Converter;

/**
 * @author scott
 *
 */
@Path("/operator")
public class OperatorRest extends CURDRest<Operator>{
	

	@Resource(name="operatorMgmt")
	private OperatorMgmtImpl operatorMgmt;
	
    @GET
    @Path("/{operatorId}")
    @Produces("application/json")
	public Operator get(@PathParam("operatorId")Integer operatorId){
			checkPermission(null);
			Operator operator = operatorMgmt.get(operatorId);
			return operator;
    }
    
    @POST
    @Path("/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingOperationResult listPage(@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
			checkPermission(null);
			return new PagingOperationResult(operatorMgmt.listByPaging("operator.all", Collections.<String, Object> emptyMap(), startPage, pageSize));
    }
    
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Operator> list(@Context UriInfo uriInfo){
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
    }  
    
	@Override
	protected PutOperationResult _create(Operator obj) {
    	checkPermission(null);
    	PutOperationResult result = new PutOperationResult();
		Integer id = operatorMgmt.add(obj);
		result.setErrorCode(ErrorCode.SUCCESS);
		result.setId(id.intValue());
		return result;
	}

	@Override
	protected OperationResult _update(Operator operator, Integer id) {
    	checkPermission(null);
    	OperationResult result = new OperationResult();
		operator.setId(id);
		operatorMgmt.update(id,operator);
		result.setErrorCode(ErrorCode.SUCCESS);
		return result;
	}   
	
	
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public PutOperationResult create(Operator operator){
    	return _create(operator);
    }
    
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public OperationResult update(Operator operator, @PathParam("id")Integer id){
    	return _update(operator,id);
    }

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#createObject()
	 */
	@Override
	protected Operator createObject() {
		return new Operator();
	}
	
	
	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#delete(java.lang.Integer)
	 */
	@Override
	protected OperationResult _delete(Integer id) {
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
    	
    		OperationResult result = new OperationResult();
			Operator operator = getCurrentOperator();
			operatorMgmt.updatePassword(operator.getId(), oldpass, newpass);
			result.setErrorCode(ErrorCode.SUCCESS);
			return result;
    }
  
    
    @POST
    @Path("/resetpass")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public OperationResult resetPassword(@FormParam("id")Integer operatorId){
    		checkPermission(null);
    		OperationResult result = new OperationResult();
			operatorMgmt.resetPassword(operatorId);
			result.setErrorCode(ErrorCode.SUCCESS);
			return result;
    }


}
