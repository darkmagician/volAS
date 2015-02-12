/**
 * 
 */
package com.vol.common.util;

/**
 * @author scott
 *
 */
final public class StringParser {

	
	public static Long parseLong(String s){
		if(s==null){
			return null;
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	public static Integer parseInteger(String s){
		if(s==null){
			return null;
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
