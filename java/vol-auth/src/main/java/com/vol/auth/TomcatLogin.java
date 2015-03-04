/**
 * 
 */
package com.vol.auth;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;


public class TomcatLogin extends RealmBase {
	
	private final AuthenticationServiceHolder holder = AuthenticationServiceHolder.getInstance();
	
	
	private String name;
	
	public TomcatLogin(){
		setDigest("MD5");
	}

	/* (non-Javadoc)
	 * @see org.apache.catalina.realm.RealmBase#getPassword(java.lang.String)
	 */
	@Override
	protected String getPassword(String username) {
		AuthenticationService service = holder.getService(name);
		if(service == null){
			System.err.println("authentication service is not ready! getPassword failed.");
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		String credentials = service.getCredential(username, context );
		return credentials;
	}

	/* (non-Javadoc)
	 * @see org.apache.catalina.realm.RealmBase#getPrincipal(java.lang.String)
	 */
	@Override
	protected Principal getPrincipal(String username) {
		AuthenticationService service = holder.getService(name);
		if(service == null){
			System.err.println("authentication service is not ready! getPrincipal failed.");
			return null;
		}
		Map<String, Object> context = new HashMap<String, Object>();
		String credentials = service.getCredential(username, context );
		if(credentials == null){
			return null;
		}
		String[] roles = service.getRoles(username, context);
		
        return new GenericPrincipal(username,credentials, Arrays.asList(roles));
	}


	@Override
	protected boolean compareCredentials(String userCredentials,
			String serverCredentials) {
		if(serverCredentials.startsWith(AuthenticationService.MD5_PREFIX)){
			   String userDigest = digest(userCredentials);
			   String serverDigest = serverCredentials.substring(AuthenticationService.MD5_PREFIX.length());
			   return serverDigest.equalsIgnoreCase(userDigest);
		}else{
			return serverCredentials.equalsIgnoreCase(userCredentials);
		}
     
        
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
