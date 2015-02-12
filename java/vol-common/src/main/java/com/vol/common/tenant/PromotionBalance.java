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
	 * The max.
	 */
	private long max;
	
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
	 * Gets the max.
	 *
	 * @return the max
	 */
	public long getMax() {
		return max;
	}
	
	/**
	 * Sets the max.
	 *
	 * @param max
	 *            the max to set
	 */
	public void setMax(long max) {
		this.max = max;
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
		builder.append(", max=");
		builder.append(max);
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
