/**
 * 
 */
package com.vol.auth;

import java.util.Map;

/**
 * The Interface AuthenticationService.
 *
 * @author scott
 */
public interface AuthenticationService {
	
	public static final String MD5_PREFIX = "MD5:";
	/**
	 * Gets the credential.
	 *
	 * @param userName
	 *            the user name
	 * @param context
	 *            the context
	 * @return the credential
	 */
	public String getCredential(String userName, Map<String, Object> context);
	
	/**
	 * Gets the roles.
	 *
	 * @param userName
	 *            the user name
	 * @param context
	 *            the context
	 * @return the roles
	 */
	public String[] getRoles(String userName, Map<String, Object> context);
	
}
