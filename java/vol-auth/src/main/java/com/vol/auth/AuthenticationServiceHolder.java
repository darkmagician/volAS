/**
 * 
 */
package com.vol.auth;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The Class AuthenticationServiceHolder.
 *
 * @author scott
 */
public class AuthenticationServiceHolder {
	
	/**
	 * The Constant holder.
	 */
	private final static AuthenticationServiceHolder holder = new AuthenticationServiceHolder();
	
	/**
	 * The Constant listeners.
	 */
	private final static Set<IdentityChangeListener> listeners = new CopyOnWriteArraySet<IdentityChangeListener>();
	
	/**
	 * Instantiates a new authentication service holder.
	 */
	private AuthenticationServiceHolder(){}
	
	/**
	 * Gets the single instance of AuthenticationServiceHolder.
	 *
	 * @return single instance of AuthenticationServiceHolder
	 */
	public static AuthenticationServiceHolder getInstance(){
		return holder;
	}

	/**
	 * The service.
	 */
	private AuthenticationService service;

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public AuthenticationService getService() {
		return service;
	}

	/**
	 * Sets the service.
	 *
	 * @param service
	 *            the service to set
	 */
	public void setService(AuthenticationService service) {
		this.service = service;
	}
	
	
	/**
	 * Register.
	 *
	 * @param listener
	 *            the listener
	 * @return true, if successful
	 */
	public boolean register(IdentityChangeListener listener){
		if(listeners.contains(listener)){
			return false;
		}
		return listeners.add(listener);
	}
	
	/**
	 * Unregister.
	 *
	 * @param listener
	 *            the listener
	 * @return true, if successful
	 */
	public boolean unregister(IdentityChangeListener listener){
		return listeners.remove(listener);
	}
	
	/**
	 * Gets the listeners.
	 *
	 * @return the listeners
	 */
	public Set<IdentityChangeListener> getListeners(){
		return listeners;
	}
}
