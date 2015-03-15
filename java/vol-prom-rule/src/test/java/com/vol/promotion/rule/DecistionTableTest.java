package com.vol.promotion.rule;

import javax.annotation.Resource;

import org.junit.Test;

import com.vol.common.tenant.Promotion;

/**
 * @author scott
 *
 */
public class DecistionTableTest extends BaseTest {

	@Resource(name="DTPromotionPolicy")
	PromotionPolicySPI promotionPolicySPI;
	
	@Test
	public void testJSON(){
		
		Promotion promotion=new Promotion();
		promotion.setRule("{\"cols\":[{\"title\":\"1111\",\"src\":\"username\",\"desc\":\"\",\"defaultVal\":\"1\",\"type\":\"N\"}],\"data\":[[\"1\",\"1\"]]}");
		promotionPolicySPI.validate(promotion);
		
	}
}
