/**
 * 
 */
package com.vol.common.user;

/**
 * @author scott
 *
 */
public class QuotaHistory extends Quota {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuotaHistory() {
		super();
	}
	
	
	public QuotaHistory(Quota quota){
		this.activationTime = quota.activationTime;
		this.balance = quota.balance;
		this.expirationTime = quota.expirationTime;
		this.maximum = quota.maximum;
		this.reserved = quota.reserved;
		this.tenantId = quota.tenantId;
		this.userId = quota.userId;
		this.userName = quota.userName;
		this.volumeType = quota.volumeType;
	}

}
