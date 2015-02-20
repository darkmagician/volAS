/**
 * 
 */
package com.vol.rest.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.vol.common.tenant.Promotion;
import com.vol.mgmt.PromotionMgmtImpl;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;
import com.vol.rest.service.MapConverter.Converter;

/**
 * @author scott
 *
 */
@Path("/promotion")
public class PromotionRest extends BaseRest<Promotion>{

	@Resource(name="promotionMgmt")
	protected PromotionMgmtImpl promotionMgmt;
    
    @GET
    @Path("/{tenantId}/{promotionid}")
    @Produces("application/json")
	public Promotion get(@PathParam("tenantId")Integer tenantId, @PathParam("promotionid")Integer promotionId){
    	return promotionMgmt.get(promotionId);
    }
    
    @GET
    @Path("/{tenantId}")
    @Produces("application/json")
	public List<Promotion> list(@PathParam("tenantId")Integer tenantId, @Context UriInfo uriInfo){
    	MultivaluedMap<String, String> pathPara = uriInfo.getQueryParameters();
    	String queryName = pathPara.getFirst("query");
    	if(queryName == null || "".equals(queryName)){
    		return promotionMgmt.list("promotion.all", Collections.singletonMap("tenantId", (Object)tenantId));
    	}else{
    		Map<String,Object> map = new HashMap<String,Object>();
    		map.put("current", System.currentTimeMillis());
    		map.put("tenantId", tenantId);
    		MapConverter.convert(pathPara, map, Collections.<String, Converter> emptyMap());
    		return promotionMgmt.list("promotion."+queryName, map);
    		
    	}
    }  
    
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    public PutOperationResult create(Promotion promotion){
    	PutOperationResult result = new PutOperationResult();
    	try{
    		Integer id = promotionMgmt.add(promotion);
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
    @Consumes("application/json")
    @Produces("application/json")
    public OperationResult update(Promotion promotion,@PathParam("id")Integer id){
    	OperationResult result = new OperationResult();
    	try{
    		promotion.setId(id);
    		promotionMgmt.update(id,promotion);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update promotion, "+promotion,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
    }

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#createObject()
	 */
	@Override
	public Promotion createObject() {
		return new Promotion();
	}
	
    @POST
    @Path("/active/{id}")
    @Produces("application/json")
	public OperationResult active(@PathParam("id")Integer id){
    	OperationResult result = new OperationResult();
    	try{
    		promotionMgmt.activate(id);
    		result.setCode(PutOperationResult.SUCCESS);
    	}catch(Throwable e){
    		log.error("failed to update promotion, id="+id,e);
    		result.setCode(PutOperationResult.INTERNAL_ERROR);
    	}
		return result;
		
	}
}
