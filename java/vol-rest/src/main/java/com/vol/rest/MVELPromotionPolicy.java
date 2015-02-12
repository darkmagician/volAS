/**
 * 
 */
package com.vol.rest;

import java.util.Map;

import org.mvel2.MVEL;

import com.vol.common.service.PromotionPolicy;
import com.vol.common.tenant.Promotion;

/**
 * @author scott
 *
 */
public class MVELPromotionPolicy implements PromotionPolicy {

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant.Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {
		String rule = promotion.getRule();
		
		return  MVEL.eval(rule, context, Long.class);
	}


}
