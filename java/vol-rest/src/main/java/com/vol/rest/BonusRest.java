/**
 * 
 */
package com.vol.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author scott
 *
 */
@Path("/bonus")
public class BonusRest {
	
    @GET
    @Path("/{tenantId}/{userName}")
    @Produces("application/json")
	public void listAllActive(@PathParam("tenantId")String tenant,@PathParam("userName")String userName){
    	
    }
    
    
    @GET
    @Path("/{tenantId}/{userId}")
    @Produces("application/json")
	public void listAllActive(){
    	
    }
}
