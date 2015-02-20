/**
 * 
 */
package com.vol.rest.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

/**
 * @author scott
 *
 */
public class MapConverter  {
	interface Converter{
		Object valueOf(String value);
	}
	
	public final static Converter LONG_CONVERTER = new Converter(){
		@Override
		public Object valueOf(String value) {
			return Long.valueOf(value);
		}
		
	};
	
	public final static Converter SHORT_CONVERTER = new Converter(){
		@Override
		public Object valueOf(String value) {
			return Short.valueOf(value);
		}
		
	};
	public final static Converter INTEGER_CONVERTER = new Converter(){
		@Override
		public Object valueOf(String value) {
			return Integer.valueOf(value);
		}
		
	};
	

	/**
	 * @param delegate
	 * @param map 
	 */
	public static void convert(MultivaluedMap<String, String> delegate, Map<String,String> map) {
		for(java.util.Map.Entry<String, List<String>> entry:delegate.entrySet()){
			List<String> value = entry.getValue();
			map.put(entry.getKey(), value==null?null:value.get(0));
		}
	}
	public static void convert(MultivaluedMap<String, String> delegate, Map<String,Object> map, Map<String,Converter> converters) {
		for(java.util.Map.Entry<String, List<String>> entry:delegate.entrySet()){
			List<String> value = entry.getValue();
			if(value != null){
				String key = entry.getKey();
				Converter converter = converters.get(key);
				map.put(key, converter==null? value.get(0): converter.valueOf(value.get(0)));
			}
			
		}
	}


}
