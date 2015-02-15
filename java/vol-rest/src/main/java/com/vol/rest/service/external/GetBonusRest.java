/**
 * 
 */
package com.vol.rest.service.external;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import com.vol.rest.result.BunosResult;
import com.vol.rest.service.FormMap;

/**
 * @author scott
 *
 */

@Path("/getbonus")
public class GetBonusRest {
	@Resource
	private PromotionServiceImpl promotionService;
	
	
	@POST
	@Path("/{tenantId}/{promotionId}/givemebonus")
	@Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public BunosResult giveMeBonus(@PathParam("tenantId")Integer tenantId, @PathParam("promotionId")Integer promotionId, MultivaluedMap<String, String> params) {
	   FormMap input = new FormMap(params);
       String name = input.get("username");
       return promotionService.giveMeBonus(tenantId,promotionId,name,input);
	}
	 
}
