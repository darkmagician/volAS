/**
 * 
 */
package com.vol.rest.service;

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
public class QuotaRest {
	@Resource(name="quotaMgmt")
	protected QuotaMgmtImpl quotaMgmt;
	
    @GET
    @Path("/{userId}")
    @Produces("application/json")
	public List<Quota> list(@PathParam("userId")Long userId){
    	return quotaMgmt.getQuotasByUser(userId);
    }
    
    
    @GET
    @Path("/{userId}/{id}")
    @Produces("application/json")
	public Quota getBonus(@PathParam("userId")Long userId, @PathParam("id")Long id){
    	Quota quota = quotaMgmt.getQuota(id);
    	if(quota != null){
    		if(quota.getUserId() != userId){
    			return null;
    		}
    	}
    	return quota;
    }
}
