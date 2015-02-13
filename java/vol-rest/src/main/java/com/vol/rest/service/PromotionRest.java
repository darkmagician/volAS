/**
 * 
 */
package com.vol.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.vol.common.tenant.Promotion;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;

/**
 * @author scott
 *
 */

public class PromotionRest extends PromotionPublicRest{


    
    
    @PUT
    @Path("/")
    @Consumes({"application/json","application/x-www-form-urlencoded"})
    @Produces("application/json")
    public PutOperationResult create(Promotion promotion){
    	PutOperationResult result = new PutOperationResult();
    	try{
    		Integer id = promotionMgmt.addPromotion(promotion);
    		result.setCode(PutOperationResult.SUCCESS);
    		result.setId(id.intValue());
    	}catch(Throwable e){
    		log.error("failed to create promotion, "+promotion,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }
    
    @POST
    @Path("/")
    @Consumes({"application/json","application/x-www-form-urlencoded"})
    @Produces("application/json")
    public OperationResult update(Promotion promotion){
    	OperationResult result = new OperationResult();
    	try{
    		promotionMgmt.updatePromotion(promotion);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update promotion, "+promotion,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }
}
