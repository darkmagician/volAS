/**
 * 
 */
package com.vol.pub;

/**
 * @author scott
 *
 */
public interface CacheEntityListener<K,V> {

	
	public void onLoad(K key, V value);
	
	public void onUnload(K key, V value);
	
	public void onChange(K key, V oldvalue, V newvalue);
}
