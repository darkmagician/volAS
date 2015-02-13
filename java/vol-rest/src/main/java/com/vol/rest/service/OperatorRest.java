/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.tenant.Operator;
import com.vol.mgmt.OperatorMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;

/**
 * @author scott
 *
 */
@Path("/operator")
public class OperatorRest {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Resource(name="operatorMgmt")
	private OperatorMgmtImpl operatorMgmt;
	
    @GET
    @Path("/{operatorId}")
    @Produces("application/json")
	public Operator get(@PathParam("operatorId")Integer operatorId){
    	Operator operator = operatorMgmt.getOperator(operatorId);
    	operator.setPassword(null);
    	return operator;
    }
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Operator> list(){
		List<Operator> list = operatorMgmt.list("operator.all", Collections.<String, Object> emptyMap());
    	 if(list != null){
    		 for(Operator operator: list){
    			 operator.setPassword(null);;
    		 }
    	 }
    	 return list;
    }  
    
    @PUT
    @Path("/")
    @Consumes({"application/json","application/x-www-form-urlencoded"})
    @Produces("application/json")
    public PutOperationResult create(Operator operator){
    	PutOperationResult result = new PutOperationResult();
    	try{
    		Integer id = operatorMgmt.addOperator(operator);
    		result.setCode(PutOperationResult.SUCCESS);
    		result.setId(id.intValue());
    	}catch(Throwable e){
    		log.error("failed to create operator, "+operator,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }
    
    @POST
    @Path("/")
    @Consumes({"application/json","application/x-www-form-urlencoded"})
    @Produces("application/json")
    public OperationResult update(Operator operator){
    	OperationResult result = new OperationResult();
    	try{
    		operatorMgmt.updateOperator(operator);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update operator, "+operator,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }
}
