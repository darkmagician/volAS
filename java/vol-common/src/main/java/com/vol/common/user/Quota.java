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
	protected long id;
	
	/**
	 * The user id.
	 */
	protected long userId;
	
	/**
	 * The maximum.
	 */
	protected long maximum;
	
	/**
	 * The balance.
	 */
	protected long balance;
	
	/**
	 * 
	 */
	protected long reserved;
	
	/**
	 * The activation time.
	 */
	protected long activationTime;
	
	/**
	 * The expiration time.
	 */
	protected long expirationTime;
	
	/**
	 * The volume type.
	 */
	protected short volumeType;
	
	/**
	 * The user name.
	 */
	protected String userName;
	
	/**
	 * The tenant id.
	 */
	protected int tenantId;

	
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the maximum.
	 *
	 * @return the maximum
	 */
	public long getMaximum() {
		return maximum;
	}

	/**
	 * Sets the maximum.
	 *
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(long maximum) {
		this.maximum = maximum;
	}

	/**
	 * Gets the balance.
	 *
	 * @return the balance
	 */
	public long getBalance() {
		return balance;
	}

	/**
	 * Sets the balance.
	 *
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}

	/**
	 * Gets the activation time.
	 *
	 * @return the activationTime
	 */
	public long getActivationTime() {
		return activationTime;
	}

	/**
	 * Sets the activation time.
	 *
	 * @param activationTime
	 *            the activationTime to set
	 */
	public void setActivationTime(long activationTime) {
		this.activationTime = activationTime;
	}

	/**
	 * Gets the expiration time.
	 *
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Sets the expiration time.
	 *
	 * @param expirationTime
	 *            the expirationTime to set
	 */
	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	/**
	 * Gets the volume type.
	 *
	 * @return the volumeType
	 */
	public short getVolumeType() {
		return volumeType;
	}

	/**
	 * Sets the volume type.
	 *
	 * @param volumeType
	 *            the volumeType to set
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
		builder.append(", reserved=");
		builder.append(reserved);
		builder.append(", activationTime=");
		builder.append(activationTime);
		builder.append(", expirationTime=");
		builder.append(expirationTime);
		builder.append(", volumeType=");
		builder.append(volumeType);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", tenantId=");
		builder.append(tenantId);
		builder.append(", creationTime=");
		builder.append(creationTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the tenant id.
	 *
	 * @return the tenantId
	 */
	public int getTenantId() {
		return tenantId;
	}

	/**
	 * Sets the tenant id.
	 *
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the reserved
	 */
	public long getReserved() {
		return reserved;
	}

	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(long reserved) {
		this.reserved = reserved;
	}
	

}
