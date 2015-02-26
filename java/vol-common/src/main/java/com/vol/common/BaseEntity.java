/**
 * 
 */
package com.vol.common;

import java.io.Serializable;


/**
 * The Class BaseEntity.
 *
 * @author scott
 */
public abstract class BaseEntity implements Serializable{
	static final public short UNSPECIFIED=0, DRAFT=1, ACTIVE=2;

	/** The creation time. */
	protected long creationTime;
	
	/** The update time. */
	protected long updateTime;
	
	/**
	 * The status.
	 */
	protected short status;
	/**
	 * @return the status
	 */
	public short getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(short status) {
		this.status = status;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	/**
	 * Gets the update time.
	 *
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * Sets the update time.
	 *
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseEntity [creationTime=");
		builder.append(creationTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	
}
