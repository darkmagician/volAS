/**
 * 
 */
package com.vol.common.tenant;

import com.vol.common.BaseEntity;


/**
 * The Class Tenant.
 *
 * @author scott
 */
public class Tenant extends BaseEntity {
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The name.
	 */
	private String name;
	
	/**
	 * The description.
	 */
	private String description;
	/**
	 * 
	 */
	private int cycleType;
	
	
	/**
	 * 
	 */
	private String ratingPlan;
	
	private long cycleStart;
	private long cycleEnd;
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tenant [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", cycleType=");
		builder.append(cycleType);
		builder.append(", ratingPlan=");
		builder.append(ratingPlan);
		builder.append(", cycleStart=");
		builder.append(cycleStart);
		builder.append(", cycleEnd=");
		builder.append(cycleEnd);
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
	 * @return the cycleType
	 */
	public int getCycleType() {
		return cycleType;
	}

	/**
	 * @param cycleType the cycleType to set
	 */
	public void setCycleType(int cycleType) {
		this.cycleType = cycleType;
	}

	/**
	 * @return the ratingPlan
	 */
	public String getRatingPlan() {
		return ratingPlan;
	}

	/**
	 * @param ratingPlan the ratingPlan to set
	 */
	public void setRatingPlan(String ratingPlan) {
		this.ratingPlan = ratingPlan;
	}

	/**
	 * @return the cycleStart
	 */
	public long getCycleStart() {
		return cycleStart;
	}

	/**
	 * @param cycleStart the cycleStart to set
	 */
	public void setCycleStart(long cycleStart) {
		this.cycleStart = cycleStart;
	}

	/**
	 * @return the cycleEnd
	 */
	public long getCycleEnd() {
		return cycleEnd;
	}

	/**
	 * @param cycleEnd the cycleEnd to set
	 */
	public void setCycleEnd(long cycleEnd) {
		this.cycleEnd = cycleEnd;
	}
}
