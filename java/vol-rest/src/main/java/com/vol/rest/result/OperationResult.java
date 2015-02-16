/**
 * 
 */
package com.vol.rest.result;

/**
 * @author scott
 *
 */
public class OperationResult {
	static final public short SUCCESS=2001,BUSY=3001;
	static final public short UNLUCKY=4001, PROMOTION_USEDUP=4002;		
	static final public short INTERNAL_ERROR=5001, INVALID_PROMOTION=5002;
	
	protected short code;
	protected String message;

	/**
	 * @return the code
	 */
	public short getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(short code) {
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
}