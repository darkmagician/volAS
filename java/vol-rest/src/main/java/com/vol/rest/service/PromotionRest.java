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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.vol.common.BaseEntity;
import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.mgmt.PagingResult;
import com.vol.common.tenant.Operator;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.PromotionBalance;
import com.vol.common.util.StringParser;
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
    	try {
			checkPermission(tenantId);
			return promotionMgmt.get(promotionId);
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }
    
    @GET
    @Path("/{tenantId}")
    @Produces("application/json")
	public List<Promotion> list(@PathParam("tenantId")Integer tenantId, @Context UriInfo uriInfo){
    	try {
			checkPermission(tenantId);
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
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }  
    
    @PUT
    @Path("/{tenantId}")
    @Consumes("application/json")
    @Produces("application/json")
    public PutOperationResult create(Promotion promotion, @PathParam("tenantId")Integer tenantId){
    	promotion.setTenantId(tenantId);
    	return create(promotion);
    }
    @POST
    @Path("/{tenantId}/{promotionid}")
    @Consumes("application/json")
    @Produces("application/json")
    public OperationResult update(Promotion promotion,@PathParam("tenantId")Integer tenantId,@PathParam("promotionid")Integer id){
    	promotion.setTenantId(tenantId);
    	return update(promotion, id);
    }
    
    public OperationResult update(Promotion promotion,Integer id){
    	checkPermission(promotion.getTenantId());
    	OperationResult result = new OperationResult();
		promotion.setId(id);
		promotionMgmt.update(id,promotion);
		result.setErrorCode(ErrorCode.SUCCESS);
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
    	try {
			OperationResult result = new OperationResult();
			 Operator operator = getCurrentOperator();
			promotionMgmt.activate(operator.getTenantId(),id);
			result.setErrorCode(ErrorCode.SUCCESS);
			return result;
		}  catch (MgmtException me) {
			log.error("Operation Error",me);
			OperationResult result = new OperationResult();
			result.setErrorCode(me.getCode());
			return result;
		} catch (Exception e){
			log.error("Operation Error",e);
			OperationResult result = new OperationResult();
			result.setErrorCode(ErrorCode.INTERNAL_ERROR);
			return result;
		}
		
	}
    
    
    
    @DELETE
    @Path("/{tenantId}/{promotionid}")
    @Produces("application/json")
	public void delete(@PathParam("tenantId")Integer tenantId, @PathParam("promotionid")Integer promotionId){
    	delete(promotionId);
    }
    
    
    @POST
    @Path("/{tenantId}/draftpaging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Promotion> listPageByDraft(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
		try {
			checkPermission(tenantId);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tenantId", tenantId);
			return promotionMgmt.listByPaging("promotion.byDraft", map, startPage, pageSize);
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }
    
    @POST
    @Path("/{tenantId}/activepaging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Promotion> listPageByActive(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize){
		try {
			checkPermission(tenantId);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tenantId", tenantId);
			map.put("current", System.currentTimeMillis());
			PagingResult<Promotion> result = promotionMgmt.listByPaging("promotion.byNotEnded", map, startPage, pageSize);
			fillBalance(result);
			return result;
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }

    @POST
    @Path("/{tenantId}/historypaging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Promotion> listPageByHistory(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize,
			@FormParam("from")String from, @FormParam("to")String to, @FormParam("name")String name){
    	try {
			checkPermission(tenantId);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tenantId", tenantId);
			map.put("current", System.currentTimeMillis());
			map.put("fromTime", StringParser.parseLong(from));
			map.put("toTime", StringParser.parseLong( to));
			map.put("name", name);
			
			PagingResult<Promotion> result = promotionMgmt.searchHistory( map, startPage, pageSize);
			fillBalance(result);
			return result;
		} catch (Exception e) {
			log.error("Operation Error",e);
			return null;
		}
    }
	/**
	 * @param result
	 */
	private void fillBalance(PagingResult<Promotion> result) {
		if(result != null){
			for(Promotion prom: result.getRows()){
				if(prom.getStatus()!= BaseEntity.DRAFT){
					int id = prom.getId();
					List<PromotionBalance> balances = promotionMgmt.getPromotionBalanceByPromotion(id);
					if(balances != null){
						long balance = 0;
						for(PromotionBalance b: balances){
							balance+=b.getBalance();
						}
						prom.setBalance(balance);
					}
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#create(java.lang.Object)
	 */
	@Override
    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
	public PutOperationResult create(Promotion promotion) {
		checkPermission(promotion.getTenantId());
    	PutOperationResult result = new PutOperationResult();
		Integer id = promotionMgmt.add(promotion);
		result.setErrorCode(ErrorCode.SUCCESS);
		result.setId(id.intValue());
		return result;
	}

	/* (non-Javadoc)
	 * @see com.vol.rest.service.BaseRest#delete(java.lang.Integer)
	 */
	@Override
	public OperationResult delete(Integer id) {
		 OperationResult result = new OperationResult();
		 Operator operator = getCurrentOperator();
		 delete(operator.getTenantId(), id);
		result.setErrorCode(ErrorCode.SUCCESS);
		 return result;
	}
    
}
