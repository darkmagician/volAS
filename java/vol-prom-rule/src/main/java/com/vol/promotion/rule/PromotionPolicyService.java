/**
 * 
 */
package com.vol.promotion.rule;

import java.util.LinkedHashMap;
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
	
	private static final Map<String,Object> allSource = new LinkedHashMap<String,Object>();
	
	static{
		allSource.put(PromotionPolicy.NOW, "");
		allSource.put(PromotionPolicy.RAND, "");
		allSource.put(PromotionPolicy.USER_NAME, "");
		allSource.put(PromotionPolicy.BONUS_VOLUME, "");
		allSource.put(PromotionPolicy.BONUS_NUMBER, "");
	}
	
	public static String[] getSourceList(){
		return  allSource.keySet().toArray(new String[0]);
	}
	
	
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
		if(promotion.getCompiled()==null){
			impl.precompile(promotion);
		}
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
