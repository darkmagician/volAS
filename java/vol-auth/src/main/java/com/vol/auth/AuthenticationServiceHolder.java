/**
 * 
 */
package com.vol.auth;

/**
 * @author scott
 *
 */
public class AuthenticationServiceHolder {
	
	private final static AuthenticationServiceHolder holder = new AuthenticationServiceHolder();
	
	private AuthenticationServiceHolder(){}
	
	public static AuthenticationServiceHolder getInstance(){
		return holder;
	}

	private AuthenticationService service;

	/**
	 * @return the service
	 */
	public AuthenticationService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(AuthenticationService service) {
		this.service = service;
	}
}
