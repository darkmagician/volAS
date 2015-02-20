/**
 * 
 */
package com.vol.rest.service.external;

import java.util.HashMap;
import java.util.Map;

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
import com.vol.rest.service.MapConverter;


/**
 * The Class BonusRequestRest.
 */
@Path("/")
public class BonusAction {
	
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
    	Map<String,String> input = new HashMap<String,String>();
    	MapConverter.convert(params, input);
	   Integer promotionId = StringParser.parseInteger(input.get("promotionId"));
       String name = input.get("userName");
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
    @Path("activebonus/{tenantId}")
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
    public boolean transfer(@PathParam("tenantId")Integer tenant,@FormParam("userId")Long fromUserId, @FormParam("fromUser")String fromUserName,@FormParam("bonusId")Long bonusId, @FormParam("toUser")String toUser){
    	return bonusMgmt.transfer(tenant, bonusId, fromUserId, fromUserName, toUser);
    }	 
}
