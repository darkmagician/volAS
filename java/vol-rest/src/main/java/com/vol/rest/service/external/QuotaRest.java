/**
 * 
 */
package com.vol.rest.service.external;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
	
    @GET
    @Path("/{tenantId}/{userId}")
    @Produces("application/json")
	public List<Quota> list(@PathParam("tenantId")Integer tenantid, @PathParam("userId")Long userId){
    	return quotaMgmt.getQuotasByUser(userId);
    }
    
    
    @GET
    @Path("/{tenantId}/{userId}/{id}")
    @Produces("application/json")
	public Quota getBonus(@PathParam("tenantId")Integer tenantid, @PathParam("userId")Long userId, @PathParam("id")Long id){
    	Quota quota = quotaMgmt.getQuota(id);
    	if(quota != null){
    		if(quota.getUserId() != userId || quota.getTenantId() != tenantid){
    			return null;
    		}
    	}
    	return quota;
    }
}
