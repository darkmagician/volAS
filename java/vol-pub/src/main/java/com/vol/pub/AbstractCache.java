/**
 * 
 */
package com.vol.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;

import org.springframework.scheduling.TaskScheduler;

import com.vol.common.BaseEntity;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public abstract class AbstractCache<K extends Serializable, V extends BaseEntity> extends AbstractQueryService<K, V> {

	private final List<CacheEntityListener<K,V>> listeners = new CopyOnWriteArrayList<CacheEntityListener<K,V>>();
	
	@Resource(name="cacheExecutor")
	private TaskScheduler scheduler;
	
	private ScheduledFuture<?> task;
	
	private int interval = 3000;
	
	public void init(){
		super.init();
		Runnable refreshJob = new Runnable(){
			@Override
			public void run() {
				sync();
			}
		};
		task = scheduler.scheduleAtFixedRate(refreshJob, new Date(), interval);
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
	
	protected void onLoad(List<Runnable> actions,final K key, final V obj){
		actions.add(new Runnable(){

			@Override
			public void run() {
				for(CacheEntityListener<K,V> listener: listeners){
					listener.onLoad(key, obj);
				}
			}
		});
	}
	
	protected void onUnload(List<Runnable> actions,final K key, final V obj){
		actions.add(new Runnable(){

			@Override
			public void run() {
				for(CacheEntityListener<K,V> listener: listeners){
					listener.onUnload(key, obj);
				}
			}
		});

	}
	
	
	protected void onChange(List<Runnable> actions,final K key, final V oldobj, final V newobj){

		actions.add(new Runnable(){

			@Override
			public void run() {
				for(CacheEntityListener<K,V> listener: listeners){
					listener.onChange(key, oldobj, newobj);
				}
				
			}
		});
	}
	
	protected void notify(List<Runnable> actions){
		for(Runnable run: actions){
			run.run();
		}
	}
	
	
	protected boolean isChanged(V oldobj, V newobj) {
		return !newobj.equals(oldobj);
	}
}
