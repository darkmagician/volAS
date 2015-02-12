package com.vol.rest;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.MessageContext;

import com.vol.common.service.BunosResult;
import com.vol.common.tenant.Promotion;
import com.vol.common.util.StringParser;

@Path("/promotion")
public class PromotionRest{
	@Context 
	private	MessageContext mc; 
	
	@Resource
	private PromotionServiceImpl promotionService;
	
	
	@POST
	@Path("/{tenantId}/{promotionId}/givemebonus")
	@Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public BunosResult giveMeBonus(@PathParam("tenantId")String tenant, @PathParam("promotionId")String promotion, MultivaluedMap<String, String> params) {
	   Integer tenantId = StringParser.parseInteger(tenant);
       Integer promotionId = StringParser.parseInteger(promotion);
	   FormMap input = new FormMap(params);
       String name = input.get("username");
       return promotionService.giveMeBonus(tenantId,promotionId,name,input);
	}
	 

    @GET
    @Path("/{tenantId}/{promotionId}")
    @Produces("application/json")
    public Promotion get(@PathParam("tenantId")String tenant,@PathParam("promotionId")String promotion) {
    	Integer tenantId = StringParser.parseInteger(tenant);
    	Integer promotionId = StringParser.parseInteger(promotion);
    	return null;
    }
    
    
    @GET
    @Path("/{tenantId}/listactive")
    @Produces("application/json")   
    public List<Promotion> listActive(){
		return null;
    	
    }
    
    @GET
    @Path("/{tenantId}/list")
    @Produces("application/json")   
    public List<Promotion> list(){
    	return null;
    } 
}

