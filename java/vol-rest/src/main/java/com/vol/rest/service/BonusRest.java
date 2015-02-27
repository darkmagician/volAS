/**
 * 
 */
package com.vol.rest.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.vol.common.mgmt.PagingResult;
import com.vol.common.user.Bonus;
import com.vol.mgmt.BonusMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/bonus")
public class BonusRest {
	@Resource(name="bonusMgmt")
	protected BonusMgmtImpl bonusMgmt;
	
    @POST
    @Path("/{tenantId}/paging")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
	public PagingResult<Bonus> listPage(@PathParam("tenantId")Integer tenantId,@FormParam("page")Integer startPage,@FormParam("rows")Integer pageSize,
			@FormParam("from")String from, @FormParam("to")String to, @FormParam("name")String name){
    	if(tenantId==null|| tenantId<=0){
    		return new PagingResult<Bonus>();
    	}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("targetUserName", name);
		map.put("tenantId", tenantId);
    	return bonusMgmt.listByPaging("bonus.byOwnedName", map, startPage, pageSize);
    }
}
