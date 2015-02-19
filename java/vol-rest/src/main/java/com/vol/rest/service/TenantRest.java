/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.vol.common.mgmt.PagingResult;
import com.vol.common.tenant.Tenant;
import com.vol.mgmt.TenantMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;

/**
 * @author scott
 *
 */
@Path("/tenant")
public class TenantRest extends BaseRest<Tenant>{

	@Resource(name="tenantMgmt")
	private TenantMgmtImpl tenantMgmt;
	
    @GET
    @Path("/{tenantId}")
    @Produces("application/json")
	public Tenant get(@PathParam("tenantId")Integer tenantId){
    	return tenantMgmt.get(tenantId);
    }
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Tenant> list(@PathParam("pageSize")Integer tenantId){
    	return tenantMgmt.list("tenant.all", Collections.<String, Object> emptyMap());
    }  
    
    @POST
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Tenant> listPage(@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
    	return tenantMgmt.listByPaging("tenant.all", Collections.<String, Object> emptyMap(), startPage, pageSize);
    }
    
    
    public PutOperationResult create(Tenant tenant){
    	PutOperationResult result = new PutOperationResult();
    	try{
    		Integer id = tenantMgmt.add(tenant);
    		result.setCode(PutOperationResult.SUCCESS);
    		result.setId(id.intValue());
    	}catch(Throwable e){
    		log.error("failed to create tenant, "+tenant,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }

    public OperationResult update(Tenant tenant, Integer id){
    	OperationResult result = new OperationResult();
    	try{
    		tenant.setId(id);
    		tenantMgmt.update(id,tenant);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update tenant, "+tenant,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#createObject()
	 */
	@Override
	public Tenant createObject() {
		return new Tenant();
	}
	
}
