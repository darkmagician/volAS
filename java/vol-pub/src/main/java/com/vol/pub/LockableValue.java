/**
 * 
 */
package com.vol.pub;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author scott
 *
 */
public class LockableValue<T> {
	private final Lock lock = new ReentrantLock();
	private T obj;
	
	
	public LockableValue() {
		super();
	}


	/**
	 * @return the lock
	 */
	public Lock getLock() {
		return lock;
	}


	/**
	 * @return the obj
	 */
	public T getObj() {
		return obj;
	}
	
	/**
	 * @param obj the obj to set
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}
	
}
