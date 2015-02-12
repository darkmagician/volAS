/**
 * 
 */
package com.vol.common.service;

import java.util.Map;

import com.vol.common.tenant.Promotion;

/**
 * The Interface PromotionPolicy.
 *
 * @author scott
 */
public interface PromotionPolicy {

	
	/**
	 * Evaluate.
	 *
	 * @param context
	 *            the context
	 * @return the long
	 */
	Long evaluate(Promotion promotion, Map<String,Object> context);
}
