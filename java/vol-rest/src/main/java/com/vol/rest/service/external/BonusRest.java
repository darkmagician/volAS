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
    @Path("/{tenantId}/{userId}")
    @Produces("application/json")
	public List<Bonus> list(@PathParam("tenantId")Integer tenantid, @PathParam("userId")Long userId){
    	return bonusMgmt.listBonusByUser(userId);
    }
    
    
    @GET
    @Path("/{tenantId}/{userId}/{bonusId}")
    @Produces("application/json")
	public Bonus getBonus(@PathParam("tenantId")Integer tenantid, @PathParam("userId")Long userId, @PathParam("userId")Long id){
    	Bonus bonus = bonusMgmt.getBonus(id);
    	if(bonus != null){
    		if(bonus.getUserId() != userId || bonus.getTenantId() != tenantid){
    			return null;
    		}
    	}
    	return bonus;
    }
    

}
