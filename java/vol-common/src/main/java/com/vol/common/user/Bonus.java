/**
 * 
 */
package com.vol.common.user;

import com.vol.common.BaseEntity;

/**
 * The Class Bonus.
 *
 * @author scott
 */
public class Bonus extends BaseEntity {

	/**
	 * The id.
	 */
	private long id;
	
	/**
	 * The user id.
	 */
	private long userId;
	
	/**
	 * The size.
	 */
	private long size;
	
	/**
	 * 
	 */
	private String targetUserName;
	
	/**
	 * The target quota id.
	 */
	private long targetQuotaId;
	
	/**
	 * The activation time.
	 */
	private long activationTime;
	
	/**
	 * 
	 */
	private long expirationTime;
	
	/**
	 * The promotion id.
	 */
	private int promotionId;
	
	/**
	 * The volume type.
	 */
	private short volumeType;
	/**
	 * The tenant id.
	 */
	private int tenantId;
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
	 * Gets the size.
	 *
	 * @return the size
	 */
	public long getSize() {
		return size;
	}
	
	/**
	 * Sets the size.
	 *
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}
	
	/**
	 * Gets the target quota id.
	 *
	 * @return the targetQuotaId
	 */
	public long getTargetQuotaId() {
		return targetQuotaId;
	}
	
	/**
	 * Sets the target quota id.
	 *
	 * @param targetQuotaId
	 *            the targetQuotaId to set
	 */
	public void setTargetQuotaId(long targetQuotaId) {
		this.targetQuotaId = targetQuotaId;
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
	 * Gets the promotion id.
	 *
	 * @return the promotionId
	 */
	public int getPromotionId() {
		return promotionId;
	}
	
	/**
	 * Sets the promotion id.
	 *
	 * @param promotionId
	 *            the promotionId to set
	 */
	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
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

	/**
	 * @return the tenantId
	 */
	public int getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
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
	 * @return the targetUserName
	 */
	public String getTargetUserName() {
		return targetUserName;
	}

	/**
	 * @param targetUserName the targetUserName to set
	 */
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bonus [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", size=");
		builder.append(size);
		builder.append(", targetUserName=");
		builder.append(targetUserName);
		builder.append(", targetQuotaId=");
		builder.append(targetQuotaId);
		builder.append(", activationTime=");
		builder.append(activationTime);
		builder.append(", expirationTime=");
		builder.append(expirationTime);
		builder.append(", promotionId=");
		builder.append(promotionId);
		builder.append(", volumeType=");
		builder.append(volumeType);
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
}
