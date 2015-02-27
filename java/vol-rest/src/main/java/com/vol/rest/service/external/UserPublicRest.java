/**
 * 
 */
package com.vol.rest.service.external;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.user.User;
import com.vol.mgmt.UserMgmtImpl;

/**
 * @author scott
 *
 */
@Path("/user")
public class UserPublicRest {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Resource(name="userMgmt")
	protected UserMgmtImpl userMgmt;
	
	@GET
	@Path("/{tenantId}/{userId}")
	@Produces("application/json")
	public User get(@PathParam("tenantId")Integer tenantId, @PathParam("userId")Long userId){
		User user = userMgmt.get(userId);
		if(user != null){
			if(user.getTenantId() != tenantId){
				return null;
			}
		}
		return user;
    }
	
	@GET
	@Path("/{tenantId}")
	@Produces("application/json")
	public User getbyName(@PathParam("tenantId")Integer tenantId, @QueryParam("userName")String name){
		User user = userMgmt.getUserByName(tenantId, name);
		return user;
    }
}
