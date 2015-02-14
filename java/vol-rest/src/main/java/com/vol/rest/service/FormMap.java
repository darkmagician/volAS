/**
 * 
 */
package com.vol.rest.service;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author scott
 *
 */
public class FormMap extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param delegate
	 */
	public FormMap(MultivaluedMap<String, String> delegate) {
		super();
		for(java.util.Map.Entry<String, List<String>> entry:delegate.entrySet()){
			List<String> value = entry.getValue();
			put(entry.getKey(), value==null?null:value.get(0));
		}
	}


}
