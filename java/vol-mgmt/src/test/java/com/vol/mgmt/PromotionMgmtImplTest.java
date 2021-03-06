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
		promotion.setRule("return granted.length>0? 0:11;");
		promotion.setStartTime(System.currentTimeMillis());
		promotion.setEndTime(System.currentTimeMillis()+1000);
		promotion.setBonusExpirationTime(System.currentTimeMillis()+2000);
		int id =promotionMgmt.add(promotion );
		Assert.assertTrue(id>0);
		Promotion promotion2= promotionMgmt.get(id);
		log.info("promotion2 {}",promotion2);
		Assert.assertNotNull(promotion2);
		promotion2.setName("2222");
		promotionMgmt.update(id,promotion2);
		Promotion promotion3= promotionMgmt.get(id);	
		log.info("promotion3 {}",promotion3);
		Promotion promotion4 = new Promotion();
		promotion4.setName("444");
		promotion4.setId(id);
		Exception failed=null;
		try {
			promotionMgmt.update(id,promotion4);
		} catch (Exception e) {
			e.printStackTrace();
			failed=e;
		}
		Assert.assertNotNull(failed);
		Promotion promotion5= promotionMgmt.get(id);	
		log.info("promotion5 {}",promotion5);
		Assert.assertNotNull(promotion5);
		promotion5.setRule("asdfac;");
		try {
			failed=null;
			promotionMgmt.update(id,promotion5);
		} catch (Exception e) {
			e.printStackTrace();
			failed=e;
		}
		Assert.assertNotNull(failed);
	}
	
	
	
}
