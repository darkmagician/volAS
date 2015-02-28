/**
 * 
 */
package com.vol.rest.result;

import com.vol.common.exception.ErrorCode;

/**
 * @author scott
 *
 */
public class OperationResult {
	
	protected int code;
	protected String message;
	
	
	public void setErrorCode(ErrorCode errorCode){
		code = errorCode.getCode();
		message = errorCode.getDescription();
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OperationResult [code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}
}
