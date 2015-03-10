/**
 * 
 */
package com.vol.promotion.rule;

import com.vol.common.service.PromotionPolicy;

/**
 * @author scott
 *
 */
public interface PromotionPolicySPI extends PromotionPolicy {

	public short DECISION_TABLE=0,MVEL_SCRIPT=1;
	
	public short getType();
}
