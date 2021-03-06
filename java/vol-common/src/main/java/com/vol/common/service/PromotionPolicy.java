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
	 * 
	 */
	static final String PARAMETERS = "parameters";

	/**
	 * 
	 */
	static final String ISNEWUSER = "isNewUser";

	/**
	 * 
	 */
	static final String USER = "user";

	/**
	 * 
	 */
	static final String GRANTED = "granted";

	/**
	 * 
	 */
	static final String PROMOTION_BALANCE = "promotionBalance";
	
	/**
	 * 
	 */
	static final String BALANCE = "balance";

	/**
	 * 
	 */
	static final String PROMOTION = "promotion";

	static final String NOW = "now";

	static final String RAND = "rand";
	
	static final String USER_NAME = "userName";
	
	static final String BONUS_NUMBER = "bonusNumber";
	
	static final String BONUS_VOLUME = "bonusVolume";

	public abstract void validate(Promotion promotion);


	public abstract Long evaluate(Promotion promotion, Map<String, Object> context);


	public abstract void precompile(Promotion promotion);
}
