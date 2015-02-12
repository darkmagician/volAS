/**
 * 
 */
package com.vol.common.service;

/**
 * The Class BunosResult.
 *
 * @author scott
 */
public class BunosResult {
	static final public short SUCCESS=2001,BUSY=3001;
	static final public short UNLUCKY=4001, PROMOTION_USEDUP=4002;		
	static final public short INTERNAL_ERROR=5001, INVALID_PROMOTION=5002;

	/**
	 * The bonus id.
	 */
	private long bonusId;
	
	/**
	 * The bonus size.
	 */
	private long bonusSize;
	
	/**
	 * The result code.
	 */
	private short resultCode;
	
	/**
	 * Gets the bonus id.
	 *
	 * @return the bonusId
	 */
	public long getBonusId() {
		return bonusId;
	}
	
	/**
	 * Sets the bonus id.
	 *
	 * @param bonusId
	 *            the bonusId to set
	 */
	public void setBonusId(long bonusId) {
		this.bonusId = bonusId;
	}
	
	
	/**
	 * Gets the result code.
	 *
	 * @return the resultCode
	 */
	public short getResultCode() {
		return resultCode;
	}
	
	/**
	 * Sets the result code.
	 *
	 * @param resultCode
	 *            the resultCode to set
	 */
	public void setResultCode(short resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the bonusSize
	 */
	public long getBonusSize() {
		return bonusSize;
	}

	/**
	 * @param bonusSize the bonusSize to set
	 */
	public void setBonusSize(long bonusSize) {
		this.bonusSize = bonusSize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BunosResult [bonusId=");
		builder.append(bonusId);
		builder.append(", bonusSize=");
		builder.append(bonusSize);
		builder.append(", resultCode=");
		builder.append(resultCode);
		builder.append("]");
		return builder.toString();
	}
}
