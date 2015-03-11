/**
 * 
 */
package com.vol.pub;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
		if(log.isDebugEnabled()){
			log.debug("start to sync tanent");
		}
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("current", System.currentTimeMillis());
		List<Tenant> tenants = super.list("tenant.all", parameters);
		
		List<Runnable> actions = new LinkedList<Runnable>();
		
		Map<Integer,Tenant> newcache = null;
		if(tenants != null && tenants.size() > 0){
			newcache = new HashMap<Integer,Tenant>();
			for(Tenant tenant: tenants){
				Integer id = tenant.getId();
				Tenant oldTenant = cache.get(id);
				if(oldTenant != null){
					if(isChanged(tenant, oldTenant)){
						newcache.put(id, tenant);
						onChange(actions,id, oldTenant, tenant);
					}else{
						newcache.put(id, oldTenant);
					}
				}else{
					newcache.put(id, tenant);
					onLoad(actions,id, tenant);
				}
			}
			
		}else{
			newcache = Collections.emptyMap();
		}
		
		for(Entry<Integer, Tenant> entry:cache.entrySet()){
			Integer id = entry.getKey();
			Tenant tenant = newcache.get(id);
			if(tenant == null){
				onUnload(actions,id, entry.getValue());
			}
		}
		cache = newcache;
		if(log.isDebugEnabled()){
			log.debug("Complete syncing tanent. Current size:{}",newcache.size());
		}
		notify(actions);
	}

}
