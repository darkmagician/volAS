/**
 * 
 */
package com.vol.rest.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.vol.common.user.Bonus;
import com.vol.mgmt.BonusMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/bonus")
public class BonusRest {
	@Resource(name="bonusMgmt")
	protected BonusMgmtImpl bonusMgmt;
	
    @GET
    @Path("/{userId}")
    @Produces("application/json")
	public List<Bonus> list(@PathParam("userId")Long userId){
    	return bonusMgmt.listBonusByUser(userId);
    }
    
    
    @GET
    @Path("/{userId}/{bonusId}")
    @Produces("application/json")
	public Bonus getBonus(@PathParam("userId")Long userId, @PathParam("userId")Long id){
    	Bonus bonus = bonusMgmt.getBonus(id);
    	if(bonus != null){
    		if(bonus.getUserId() != userId){
    			return null;
    		}
    	}
    	return bonus;
    }
    

    @POST
    @Path("/{tenantId}/{userId}/{bonusId}")
    @Produces("application/json")
    public boolean active(@PathParam("tenantId")Integer tenant,@PathParam("userId")Long userId, @PathParam("userId")Long bonusId, @FormParam("toUser")String targetUserName){
    	return bonusMgmt.active(tenant, bonusId, targetUserName);
    }
}
