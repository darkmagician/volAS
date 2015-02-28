/**
 * 
 */
package com.vol.auth;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
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
	private final List<AuthenticationService> services = new CopyOnWriteArrayList<AuthenticationService>();;
	
	/**
	 * The Constant listeners.
	 */
	private final Set<IdentityChangeListener> listeners = new CopyOnWriteArraySet<IdentityChangeListener>();

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public AuthenticationService getService() {
		return services.isEmpty()? null: services.get(0);
	}

	/**
	 * Sets the service.
	 *
	 * @param service
	 *            the service to set
	 */
	public void setService(AuthenticationService service) {
		this.services.add(service);
	}
	

	/**
	 * Sets the service.
	 *
	 * @param service
	 *            the service to set
	 */
	public void unsetService(AuthenticationService service) {
		this.services.remove(service);
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
