/**
 * 
 */
package com.vol.common.user;

import com.vol.common.BaseEntity;

/**
 * The Class Quota.
 *
 * @author scott
 */
public class Quota extends BaseEntity  {

	/**
	 * The id.
	 */
	private long id;
	
	/**
	 * The user id.
	 */
	private long userId;
	
	/**
	 * The maximum.
	 */
	private long maximum;
	
	/**
	 * The balance.
	 */
	private long balance;
	
	/**
	 * The activation time.
	 */
	private long activationTime;
	
	/**
	 * The expiration time.
	 */
	private long expirationTime;
	
	/**
	 * The volume type.
	 */
	private short volumeType;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the maximum
	 */
	public long getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}

	/**
	 * @return the balance
	 */
	public long getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}

	/**
	 * @return the activationTime
	 */
	public long getActivationTime() {
		return activationTime;
	}

	/**
	 * @param activationTime the activationTime to set
	 */
	public void setActivationTime(long activationTime) {
		this.activationTime = activationTime;
	}

	/**
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * @param expirationTime the expirationTime to set
	 */
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * @return the volumeType
	 */
	public short getVolumeType() {
		return volumeType;
	}

	/**
	 * @param volumeType the volumeType to set
	 */
	public void setVolumeType(short volumeType) {
		this.volumeType = volumeType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Quota [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", maximum=");
		builder.append(maximum);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", activationTime=");
		builder.append(activationTime);
		builder.append(", expirationTime=");
		builder.append(expirationTime);
		builder.append(", volumeType=");
		builder.append(volumeType);
		builder.append(", creationTime=");
		builder.append(creationTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	

}
