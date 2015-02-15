/**
 * 
 */
package com.vol.rest.service.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.vol.common.tenant.Promotion;
import com.vol.mgmt.PromotionMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/promotion")
public class PromotionPublicRest{

	@Resource(name="promotionMgmt")
	protected PromotionMgmtImpl promotionMgmt;
	
	
	
	@GET
	@Path("/{tenantId}/{promotionId}")
	@Produces("application/json")
	public Promotion get(@PathParam("tenantId")Integer tenantId, @PathParam("promotionId")Integer promotionId){
		Promotion promotion = promotionMgmt.getPromotion(promotionId);
		if(promotion != null){
			if(promotion.getTenantId() != tenantId){
				return null;
			}
		}
		return promotion;
    }

    @GET
	@Path("/{tenantId}")
    @Produces("application/json")
	public List<Promotion> list(@PathParam("tenantId")Integer tenantId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("current", System.currentTimeMillis());
		map.put("tenantId", tenantId);
		return promotionMgmt.list("promotion.byStarted", map);
		
    }
}
