/**
 * 
 */
package com.vol.rest;

import java.util.Collections;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.PromotionBalance;
import com.vol.rest.result.BunosResult;
import com.vol.rest.service.PromotionServiceImpl;

/**
 * @author scott
 *
 */
public class TestPromotionService extends BaseTest {
	@Resource
	PromotionServiceImpl promotionService;
	@Resource(name="promotionDao")
	protected DAO<Integer,Promotion> promotionDAO;
	@Resource(name="promotionBalanceDao")
	protected DAO<Integer,PromotionBalance> promotionBalanceDAO;
	@Autowired
	protected PlatformTransactionManager txManager;
	
	protected final TransactionTemplate transaction = new TransactionTemplate();
	
	@Test
	public void testGiveMeBonus1(){
		String ruleDef = "return 4000000";
		
		Promotion promotion = init(ruleDef);
		String userName = "scott";
		
		BunosResult result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
		
		Assert.assertEquals(4000000, result.getBonus().getSize());
	}
	
	
	@Test
	public void testGiveMeBonus2(){
		String ruleDef = "return isNewUser?20000000:0";
		
		Promotion promotion = init(ruleDef);
		String userName = "scott";
		
		BunosResult result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
		
		Assert.assertEquals(20000000, result.getBonus().getSize());
		result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
	//	Assert.assertEquals(0, result.getBonus().getSize());
		Assert.assertEquals(BunosResult.UNLUCKY, result.getCode());
	}
	
	@Test
	public void testGiveMeBonus3(){
		String ruleDef = "return granted.size()>=2?0:22000000";
		
		Promotion promotion = init(ruleDef);
		String userName = "scott";
		
		BunosResult result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
		
		Assert.assertEquals(22000000, result.getBonus().getSize());
		result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
		
		Assert.assertEquals(22000000, result.getBonus().getSize());
		
		result = promotionService.giveMeBonus(promotion.getTenantId(), promotion.getId(),userName, Collections.EMPTY_MAP);
		log.info("result {}",result);
		//Assert.assertEquals(0, result.getBonus().getSize());
		Assert.assertEquals(BunosResult.UNLUCKY, result.getCode());
	}

	/**
	 * @param ruleDef
	 * @return
	 */
	private Promotion init(String ruleDef) {
		final Promotion promotion= new Promotion();
		promotion.setMax(1000000000);
		promotion.setDescription("Test");
		promotion.setEndTime(System.currentTimeMillis()+100000);
		promotion.setStartTime(System.currentTimeMillis()-100);
		promotion.setName("Test1");
		promotion.setRule(ruleDef);
		
		
		promotion.setStatus(BaseEntity.ACTIVE);
		long now = System.currentTimeMillis();
		promotion.setUpdateTime(now);
		promotion.setCreationTime(now);
		Integer promotionId = promotionDAO.create(promotion);
		
		
		PromotionBalance balance = new PromotionBalance();
		balance.setBalance(promotion.getMax());
		balance.setMax(promotion.getMax());
		balance.setUpdateTime(now);
		balance.setCreationTime(now);
		balance.setStatus(BaseEntity.ACTIVE);
		balance.setPromotionId(promotionId);
		promotionBalanceDAO.create(balance);
		return promotion;
	}
}
