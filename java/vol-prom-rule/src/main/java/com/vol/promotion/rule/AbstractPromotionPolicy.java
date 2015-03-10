/**
 * 
 */
package com.vol.promotion.rule;

import javax.annotation.Resource;

/**
 * @author scott
 *
 */
public abstract class AbstractPromotionPolicy implements PromotionPolicySPI{

	@Resource(name="facadePromotionPolicyService")
	protected PromotionPolicyService facade;
	
	public void init(){
		facade.register(this);
	}
}
