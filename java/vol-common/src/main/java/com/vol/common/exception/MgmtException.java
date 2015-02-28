/**
 * 
 */
package com.vol.common.exception;



public class MgmtException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final ErrorCode code;


	public MgmtException(ErrorCode code,String message, Throwable cause) {
		super(constructMessage(code, message), cause);
		this.code = code;
	}

	public MgmtException(ErrorCode code,String message) {
		super(constructMessage(code, message));
		this.code = code;
	}


	public MgmtException(ErrorCode code,Throwable cause) {
		super(constructMessage(code, null),cause);
		this.code = code;
	}


	public MgmtException(ErrorCode code) {
		super();
		this.code = code;
	}


	public ErrorCode getCode() {
		return code;
	}
	
	private static String constructMessage(ErrorCode code, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("Code:").append(code.getCode());
		if(message != null){
			sb.append(", Msg:").append(message);
		}
		
		return sb.toString();
	}

}
