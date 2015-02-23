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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

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
    	Operator operator = operatorMgmt.get(operatorId);
    	operator.setPassword(null);
    	return operator;
    }
    
    @POST
    @Path("/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Operator> listPage(@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
    	return operatorMgmt.listByPaging("operator.all", Collections.<String, Object> emptyMap(), startPage, pageSize);
    }
    
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Operator> list(@Context UriInfo uriInfo){
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
    
    
    public PutOperationResult create(Operator operator){
    	PutOperationResult result = new PutOperationResult();
    	try{
    		Integer id = operatorMgmt.add(operator);
    		result.setCode(PutOperationResult.SUCCESS);
    		result.setId(id.intValue());
    	}catch(Throwable e){
    		log.error("failed to create operator, "+operator,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }
    
    public OperationResult update(Operator operator, @PathParam("id")Integer id){
    	OperationResult result = new OperationResult();
    	try{
    		operator.setId(id);
    		operatorMgmt.update(id,operator);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update operator, "+operator,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
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
		OperationResult result = new OperationResult();
    	try{
    		operatorMgmt.delete(id);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to delete operator, "+id,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
	}
}
