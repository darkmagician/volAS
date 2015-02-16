/**
 * 
 */
package com.vol.rest.service;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.tenant.PromotionBalance;
import com.vol.mgmt.PromotionMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/promotionbalance")
public class PromotionBalanceRest {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource(name="promotionMgmt")
	protected PromotionMgmtImpl promotionMgmt;
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public PromotionBalance get( @PathParam("id")Integer id){
		PromotionBalance balance = promotionMgmt.getPromotionBalance(id);
		return balance;
    }
	
	@GET
	@Path("/")
	@Produces("application/json")
	public PromotionBalance getByPromotion( @QueryParam("promotionId")Integer promotionId){
		PromotionBalance balance = promotionMgmt.getPromotionBalance(promotionId);
		return balance;
    }	
}
