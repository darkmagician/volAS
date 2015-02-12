/**
 * 
 */
package com.vol.mgmt;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.annotation.NotTransactional;

import com.vol.common.tenant.Promotion;

/**
 * @author scott
 *
 */
public class PromotionMgmtImplTest extends BaseTest{
	@Resource
	PromotionMgmtImpl promotionMgmt;
	
	@Test
	@NotTransactional
	public void testPromotion(){
		Promotion promotion = new Promotion();
		promotion.setName("111");
		int id =promotionMgmt.addPromotion(promotion );
		Assert.assertTrue(id>0);
		Promotion promotion2= promotionMgmt.getPromotion(id);
		log.info("promotion2 {}",promotion2);
		Assert.assertNotNull(promotion2);
		promotion2.setName("2222");
		promotionMgmt.updatePromotion(promotion2);
		Promotion promotion3= promotionMgmt.getPromotion(id);	
		log.info("promotion3 {}",promotion3);
		Promotion promotion4 = new Promotion();
		promotion4.setName("444");
		promotion4.setId(id);
		Exception failed=null;
		try {
			promotionMgmt.updatePromotion(promotion4);
		} catch (Exception e) {
			failed=e;
		}
		Assert.assertNotNull(failed);
		Promotion promotion5= promotionMgmt.getPromotion(id);	
		log.info("promotion5 {}",promotion5);
		Assert.assertNotNull(promotion5);
	}
}
