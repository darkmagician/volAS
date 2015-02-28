/**
 * 
 */
package com.vol.common.exception;

/**
 * @author Dai Zhi Jie
 *
 */
public enum ErrorCode {
	SUCCESS(2001),
	INTERNAL_ERROR(5001)
	;
	
    private final int code;
    private final String description;
    
    
    private ErrorCode( int code) {
        this.code = code;
        description=null;
    }
    
    private ErrorCode( int code, String description) {
        this.description = description;
        this.code = code;
    }
    
	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
}
