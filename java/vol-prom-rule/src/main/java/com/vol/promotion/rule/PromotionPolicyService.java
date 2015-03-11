/**
 * 
 */
package com.vol.promotion.rule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vol.common.service.PromotionPolicy;
import com.vol.common.tenant.Promotion;

/**
 * @author scott
 *
 */
public class PromotionPolicyService implements PromotionPolicy {
	
	private Map<Short,PromotionPolicySPI> spi = new ConcurrentHashMap<Short,PromotionPolicySPI>();
	
	
	public void register(PromotionPolicySPI impl){
		spi.put(impl.getType(), impl);
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#validate(com.vol.common.tenant.Promotion)
	 */
	@Override
	public void validate(Promotion promotion) {
		short type = promotion.getRuleType();
		PromotionPolicySPI impl = spi.get(type);
		impl.validate(promotion);
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant.Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {
		short type = promotion.getRuleType();
		PromotionPolicySPI impl = spi.get(type);
		return impl.evaluate(promotion,context);
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#precompile(com.vol.common.tenant.Promotion)
	 */
	@Override
	public void precompile(Promotion promotion) {
		short type = promotion.getRuleType();
		PromotionPolicySPI impl = spi.get(type);
		impl.precompile(promotion);
	}

}