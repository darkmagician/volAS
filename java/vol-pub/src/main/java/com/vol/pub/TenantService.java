/**
 * 
 */
package com.vol.pub;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public class TenantService extends AbstractQueryService<Integer,Tenant> {

	private ScheduledExecutorService scheduler;

	@Resource(name="tenantDao")
	protected DAO<Integer,Tenant> tenantDAO;
	
	protected Map<Integer,Tenant> cache = Collections.emptyMap(); 
	
	private ScheduledFuture<?> task;

	@Override 
	protected DAO<Integer, Tenant> getDAO() {
		return tenantDAO;
	}
	
	public void init(){
		super.init();
		Runnable refreshJob = new Runnable(){
			@Override
			public void run() {
				refresh();
			}
		};
		task = scheduler.scheduleAtFixedRate(refreshJob, 10, 600,TimeUnit.SECONDS);
	}
	
	public void destroy(){
		if(task!=null){
			task.cancel(true);
		}
	}

	
	@Override
	public Tenant get(Integer id) {
		return cache.get(id);
	}
	
	public synchronized void refresh(){
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
						//change
					}else{
						newcache.put(id, oldTenant);
					}
				}else{
					newcache.put(id, tenant);
					//load
				}
			}
			
		}else{
			newcache = Collections.emptyMap();
		}
		
		for(Entry<Integer, Tenant> entry:cache.entrySet()){
			Integer id = entry.getKey();
			Tenant tenant = newcache.get(id);
			if(tenant == null){
				//unload
			}
		}
		cache = newcache;
	}

	private boolean isChanged(Tenant tenant, Tenant oldTenant) {
		return !tenant.equals(oldTenant);
	}

}
