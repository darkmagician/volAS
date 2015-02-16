/**
 * 
 */
package com.vol.rest.service.external;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;

import com.vol.common.util.StringParser;
import com.vol.mgmt.BonusMgmtImpl;
import com.vol.rest.result.BunosResult;
import com.vol.rest.service.FormMap;


// TODO: Auto-generated Javadoc
/**
 * The Class BonusRequestRest.
 */
@Path("/")
public class BonusRequestRest {
	
	/** The bonus mgmt. */
	@Resource(name="bonusMgmt")
	protected BonusMgmtImpl bonusMgmt;
	
	/** The promotion service. */
	@Resource
	private PromotionServiceImpl promotionService;
	
	/**
	 * Give me bonus.
	 *
	 * @param tenantId the tenant id
	 * @param params the params
	 * @return the bunos result
	 */
	@POST
	@Path("getbonus/{tenantId}")
	@Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public BunosResult giveMeBonus(@PathParam("tenantId")Integer tenantId, MultivaluedMap<String, String> params) {
	   FormMap input = new FormMap(params);
	   Integer promotionId = StringParser.parseInteger(input.get("promotionId"));
       String name = input.get("username");
       return promotionService.giveMeBonus(tenantId,promotionId,name,input);
	}

    /**
     * Active.
     *
     * @param tenant the tenant
     * @param userId the user id
     * @param bonusId the bonus id
     * @return true, if successful
     */
    @POST
    @Path("active/{tenantId}")
    @Produces("application/json")
    public boolean active(@PathParam("tenantId")Integer tenant, @FormParam("bonusId")Long bonusId){
    	return bonusMgmt.active(tenant, bonusId, null);
    }
    
    /**
     * Transfer.
     *
     * @param tenant the tenant
     * @param userId the user id
     * @param bonusId the bonus id
     * @return true, if successful
     */
    @PUT
    @Path("/sendbonus/{tenantId}")
    @Produces("application/json")   
    public boolean transfer(@PathParam("tenantId")Integer tenant,@FormParam("userId")Long userId, @FormParam("bonusId")Long bonusId){
    	return bonusMgmt.active(tenant, bonusId, null);
    }	 
}
