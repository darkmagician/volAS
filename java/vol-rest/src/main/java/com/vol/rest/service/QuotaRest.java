/**
 * 
 */
package com.vol.rest.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.vol.mgmt.QuotaMgmtImpl;
import com.vol.rest.result.PagingOperationResult;

/**
 * @author scott
 *
 */
@Path("/quota")
public class QuotaRest extends RestContext {
	@Resource(name="quotaMgmt")
	protected QuotaMgmtImpl quotaMgmt;
	
    @POST
    @Path("/{tenantId}/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingOperationResult listPageByDraft(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize,@FormParam("name")String name){
    	checkPermission(tenantId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tenantId", tenantId);
		map.put("userName", name);
    	return new PagingOperationResult(quotaMgmt.listByPaging("quota.byUserName", map, startPage, pageSize));
    }	
}
