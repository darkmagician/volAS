/**
 * 
 */
package com.vol.common.mgmt;

/**
 * @author scott
 *
 */
public class VolMgmtException extends RuntimeException {

	/**
	 * 
	 */
	public VolMgmtException() {
		super();
	}


	/**
	 * @param message
	 * @param cause
	 */
	public VolMgmtException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public VolMgmtException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public VolMgmtException(Throwable cause) {
		super(cause);
	}

}
