/**
 * 
 */
package com.vol.promotion.rule;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author scott
 *
 */
public abstract class AbstractPromotionPolicy implements PromotionPolicySPI{

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource(name="promotionPolicyService")
	protected PromotionPolicyService facade;
	
	public void init(){
		facade.register(this);
	}
}
