/**
 * 
 */
package com.vol.common.tenant;

import com.vol.common.BaseEntity;

/**
 * The Class PromotionBalance.
 *
 * @author scott
 */
public class PromotionBalance extends BaseEntity{

	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The promotion id.
	 */
	private int promotionId;
	
	/**
	 * The balance.
	 */
	private long balance;
	/**
	 * The reserved.
	 */
	private long reserved;	
	/**
	 * The max.
	 */
	private long maximum;
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PromotionBalance [id=");
		builder.append(id);
		builder.append(", promotionId=");
		builder.append(promotionId);
		builder.append(", balance=");
		builder.append(balance);
		builder.append(", reserved=");
		builder.append(reserved);
		builder.append(", maximum=");
		builder.append(maximum);
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
