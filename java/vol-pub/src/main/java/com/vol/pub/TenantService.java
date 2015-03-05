/**
 * 
 */
package com.vol.pub;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class TenantService extends AbstractCache<Integer,Tenant> {


	@Resource(name="tenantDao")
	protected DAO<Integer,Tenant> tenantDAO;
	
	protected Map<Integer,Tenant> cache = Collections.emptyMap(); 
	


	@Override 
	protected DAO<Integer, Tenant> getDAO() {
		return tenantDAO;
	}
	
	
	@Override
	public Tenant get(Integer id) {
		return cache.get(id);
	}
	
	public synchronized void sync(){
		Map<String,Object> parameters = new HashMap<String,Object>();
		List<Tenant> tenants = super.list("", parameters);
		
		Map<Integer,Tenant> newcache = null;
		if(tenants != null && tenants.size() > 0){
			newcache = new HashMap<Integer,Tenant>();
			for(Tenant tenant: tenants){
				Integer id = tenant.getId();
				Tenant oldTenant = cache.get(id);
				if(oldTenant != null){
					if(isChanged(tenant, oldTenant)){
						newcache.put(id, tenant);
						onChange(id, oldTenant, tenant);
					}else{
						newcache.put(id, oldTenant);
					}
				}else{
					newcache.put(id, tenant);
					onLoad(id, tenant);
				}
			}
			
		}else{
			newcache = Collections.emptyMap();
		}
		
		for(Entry<Integer, Tenant> entry:cache.entrySet()){
			Integer id = entry.getKey();
			Tenant tenant = newcache.get(id);
			if(tenant == null){
				onUnload(id, entry.getValue());
			}
		}
		cache = newcache;
	}

}
