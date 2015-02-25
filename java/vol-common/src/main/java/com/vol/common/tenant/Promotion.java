/**
 * 
 */
package com.vol.common.tenant;

import com.vol.common.BaseEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class Promotion.
 *
 * @author scott
 */
public class Promotion extends BaseEntity {
	
	public final static short MOBILE=0, WIFI=1, DIRECTION=2;

	/**
	 * The name.
	 */
	private String name;
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The description.
	 */
	private String description;
	
	
	/**
	 * The start time.
	 */
	private long startTime;
	
	/**
	 * The end time.
	 */
	private long endTime;
	
	/**
	 * The rule.
	 */
	private String rule;
	
	/**
	 * The last update operator.
	 */
	private int lastUpdateOperator;
	
	/**
	 * The tenant id.
	 */
	private int tenantId;
	
	/**
	 * The max.
	 */
	private long maximum;
	/**
	 * The volume type.
	 */
	private short volumeType;
	
	
	/** The bonus expiration time. */
	private long bonusExpirationTime;
	
	private long balance;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * Gets the start time.
	 *
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the rule.
	 *
	 * @return the rule
	 */
	public String getRule() {
		return rule;
	}

	/**
	 * Sets the rule.
	 *
	 * @param rule
	 *            the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}

	/**
	 * Gets the last update operator.
	 *
	 * @return the lastUpdateOperator
	 */
	public int getLastUpdateOperator() {
		return lastUpdateOperator;
	}

	/**
	 * Sets the last update operator.
	 *
	 * @param lastUpdateOperator
	 *            the lastUpdateOperator to set
	 */
	public void setLastUpdateOperator(int lastUpdateOperator) {
		this.lastUpdateOperator = lastUpdateOperator;
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
	 * Gets the max.
	 *
	 * @return the max
	 */
	public long getMaximum() {
		return maximum;
	}

	/**
	 * Sets the max.
	 *
	 * @param max
	 *            the max to set
	 */
	public void setMaximum(long max) {
		this.maximum = max;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Promotion [name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append(", description=");
		builder.append(description);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", rule=");
		builder.append(rule);
		builder.append(", lastUpdateOperator=");
		builder.append(lastUpdateOperator);
		builder.append(", tenantId=");
		builder.append(tenantId);
		builder.append(", maximum=");
		builder.append(maximum);
		builder.append(", volumeType=");
		builder.append(volumeType);
		builder.append(", bonusExpirationTime=");
		builder.append(bonusExpirationTime);
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
	 * Gets the bonus expiration time.
	 *
	 * @return the bonus expiration time
	 */
	public long getBonusExpirationTime() {
		return bonusExpirationTime;
	}

	/**
	 * Sets the bonus expiration time.
	 *
	 * @param bonusExpirationTime the new bonus expiration time
	 */
	public void setBonusExpirationTime(long bonusExpirationTime) {
		this.bonusExpirationTime = bonusExpirationTime;
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
	
	
	
}
