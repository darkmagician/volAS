/**
 * 
 */
package com.vol.rest.service;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import com.vol.common.util.StringParser;

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
	public BonusRest giveMeBonus(@PathParam("tenantId")String tenant, @PathParam("promotionId")String promotion, MultivaluedMap<String, String> params) {
	   Integer tenantId = StringParser.parseInteger(tenant);
       Integer promotionId = StringParser.parseInteger(promotion);
	   FormMap input = new FormMap(params);
       String name = input.get("username");
 //      return promotionService.giveMeBonus(tenantId,promotionId,name,input);
       return null;
	}
	 
}
