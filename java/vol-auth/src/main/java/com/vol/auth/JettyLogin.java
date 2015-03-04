/**
 * 
 */
package com.vol.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.security.MappedLoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;

/**
 * @author scott
 *
 */
public class JettyLogin extends MappedLoginService implements IdentityChangeListener{
	
	private final AuthenticationServiceHolder holder = AuthenticationServiceHolder.getInstance();

	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.MappedLoginService#loadUser(java.lang.String)
	 */
	@Override
	protected UserIdentity loadUser(String username) {
		AuthenticationService service = holder.getService(_name);
		if(service == null){
			System.err.println("authentication service is not ready! Login failed.");
			return null;
		}
		holder.register(this);
		Map<String, Object> context = new HashMap<String, Object>();
		String credentials = service.getCredential(username, context );
		if(credentials == null){
			return null;
		}
		String[] roles = service.getRoles(username, context);
		return putUser(username, Credential.getCredential(credentials),roles);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.MappedLoginService#loadUsers()
	 */
	@Override
	protected void loadUsers() throws IOException {
		
	}

	/* (non-Javadoc)
	 * @see com.vol.auth.IdentityChangeListener#onChange(java.lang.String)
	 */
	@Override
	public void onChange(String name) {
		System.out.println("clear operator "+name);
		this._users.remove(name);
	}

}
