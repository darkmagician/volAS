/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.vol.common.tenant.Tenant;
import com.vol.mgmt.TenantMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PagingOperationResult;
import com.vol.rest.result.PutOperationResult;
import com.vol.rest.service.MapConverter.Converter;

/**
 * @author scott
 *
 */
@Path("/tenant")
public class TenantRest extends CURDRest<Tenant>{

	@Resource(name="tenantMgmt")
	private TenantMgmtImpl tenantMgmt;
	
    @GET
    @Path("/{tenantId}")
    @Produces("application/json")
	public Tenant get(@PathParam("tenantId")Integer tenantId){
			checkPermission(tenantId);
			return tenantMgmt.get(tenantId);
    }
    
    @GET
    @Path("/")
    @Produces("application/json")
	public List<Tenant> list(@Context UriInfo uriInfo){
			checkPermission(null);
			MultivaluedMap<String, String> pathPara = uriInfo.getQueryParameters();
			String queryName = pathPara.getFirst("query");
			if(queryName == null || "".equals(queryName)){
				return tenantMgmt.list("tenant.all", Collections.<String, Object> emptyMap());
			}else{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("current", System.currentTimeMillis());
				MapConverter.convert(pathPara, map, Collections.<String, Converter> emptyMap());
				log.info("parameters,{}",pathPara);
				return tenantMgmt.list("tenant."+queryName, map);
				
			}
    }  
    
    @POST
    @Path("/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingOperationResult listPage(@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
			checkPermission(null);
			return new PagingOperationResult(tenantMgmt.listByPaging("tenant.all", Collections.<String, Object> emptyMap(), startPage, pageSize));
    }
	@Override
	protected PutOperationResult _create(Tenant obj) {
    	checkPermission(null);
    	PutOperationResult result = new PutOperationResult();
		Integer id = tenantMgmt.add(obj);
		result.setErrorCode(ErrorCode.SUCCESS);
		result.setId(id.intValue());
		return result;
	}

	@Override
	protected OperationResult _update(Tenant obj, Integer id) {
		checkPermission(null);
		OperationResult result = new OperationResult();
		obj.setId(id);
		tenantMgmt.update(id,obj);
		result.setErrorCode(ErrorCode.SUCCESS);
		return result;
	}	
      
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public PutOperationResult create(Tenant tenant){
    	return _create(tenant);
    }
    @POST
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public OperationResult update(Tenant tenant, @PathParam("id") Integer id){
    	return _update(tenant,id);
    }

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#createObject()
	 */
	@Override
	protected Tenant createObject() {
		return new Tenant();
	}

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#delete(java.lang.Integer)
	 */
	@Override
	protected OperationResult _delete(Integer id) {
		checkPermission(null);
		OperationResult result = new OperationResult();
		tenantMgmt.delete(id);
		result.setErrorCode(ErrorCode.SUCCESS);
		return result;
	}
	
    @POST
    @Path("/select")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public Map<Integer,String> listAsKeyName(){
		checkPermission(null);
		Map<Integer,String> map = new TreeMap<Integer,String>();
		 List<Tenant> tenants = tenantMgmt.list("tenant.all", Collections.<String, Object> emptyMap());
		 if(tenants != null){
			 for(Tenant t:tenants ){
				 map.put(t.getId(), t.getName());
			 }
		 }
		 return map;
    }


	
}
