/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.tenant.Promotion;
import com.vol.mgmt.PromotionMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/promotion")
public class PromotionPublicRest {
	protected final Logger log = LoggerFactory.getLogger(getClass());

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
    	return promotionMgmt.list("tenant.all", Collections.singletonMap("tenantId", (Object)tenantId));
    }  
}
