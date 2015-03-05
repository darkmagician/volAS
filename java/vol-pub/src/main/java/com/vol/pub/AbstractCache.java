/**
 * 
 */
package com.vol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vol.common.BaseEntity;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public abstract class AbstractCache<K extends Serializable, V extends BaseEntity> extends AbstractQueryService<K, V> {

	private final List<CacheEntityListener<K,V>> listeners = new CopyOnWriteArrayList<CacheEntityListener<K,V>>();
	
	private ScheduledExecutorService scheduler;
	
	private ScheduledFuture<?> task;
	
	private int inital = 10000;
	
	private int interval = 11000;
	
	public void init(){
		super.init();
		Runnable refreshJob = new Runnable(){
			@Override
			public void run() {
				sync();
			}
		};
		task = scheduler.scheduleAtFixedRate(refreshJob, inital, interval,TimeUnit.MILLISECONDS);
	}
	
	public void destroy(){
		if(task!=null){
			task.cancel(true);
		}
	}
	
	
	protected abstract void sync();
	
	public void register(CacheEntityListener<K,V> listener){
		listeners.add(listener);
	}
	
	protected void onLoad(K key, V obj){
		for(CacheEntityListener<K,V> listener: listeners){
			listener.onLoad(key, obj);
		}
	}
	
	protected void onUnload(K key, V obj){
		for(CacheEntityListener<K,V> listener: listeners){
			listener.onUnload(key, obj);
		}
	}
	
	
	protected void onChange(K key, V oldobj, V newobj){
		for(CacheEntityListener<K,V> listener: listeners){
			listener.onChange(key, oldobj, newobj);
		}
	}
	
	
	protected boolean isChanged(V oldobj, V newobj) {
		return !newobj.equals(oldobj);
	}
}
