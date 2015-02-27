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

import com.vol.common.mgmt.PagingResult;
import com.vol.common.user.Quota;
import com.vol.mgmt.QuotaMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/quota")
public class QuotaRest {
	@Resource(name="quotaMgmt")
	protected QuotaMgmtImpl quotaMgmt;
	
    @POST
    @Path("/{tenantId}/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Quota> listPageByDraft(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize,@FormParam("name")String name){
    	if(tenantId==null|| tenantId<=0){
    		return new PagingResult<Quota>();
    	}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tenantId", tenantId);
		map.put("userName", name);
    	return quotaMgmt.listByPaging("quota.byUserName", map, startPage, pageSize);
    }	
}
