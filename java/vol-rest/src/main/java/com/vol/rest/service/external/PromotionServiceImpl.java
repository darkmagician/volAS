/**
 * 
 */
package com.vol.rest.service.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.vol.common.DAO;
import com.vol.common.service.PromotionPolicy;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.PromotionBalance;
import com.vol.common.user.Bonus;
import com.vol.common.user.User;
import com.vol.dao.AbstractTransactionService;
import com.vol.rest.result.BunosResult;

/**
 * @author scott
 *
 */
public class PromotionServiceImpl extends AbstractTransactionService{

	/**
	 * 
	 */
	private static final String PARAMETERS = "parameters";

	/**
	 * 
	 */
	private static final String ISNEWUSER = "isNewUser";

	/**
	 * 
	 */
	private static final String USER = "user";

	/**
	 * 
	 */
	private static final String GRANTED = "granted";

	/**
	 * 
	 */
	private static final String PROMOTION_BALANCE = "promotionBalance";

	/**
	 * 
	 */
	private static final String PROMOTION = "promotion";
	
	@Resource(name="promotionDao")
	protected DAO<Integer,Promotion> promotionDAO;
	@Resource(name="promotionBalanceDao")
	protected DAO<Integer,PromotionBalance> promotionBalanceDAO;
	@Resource(name="userDao")
	protected DAO<Long,User> userDAO;
	@Resource(name="bonusDao")
	protected DAO<Long,Bonus> bonusDAO;	
	@Resource(name="MVELPromotionPolicy")
	protected PromotionPolicy promotionPolicy;
	
	private final ConcurrentMap<Object,Object> processingMap= new ConcurrentHashMap<Object,Object> ();
	private static final Object holder = new Object();
	
	public BunosResult giveMeBonus(Integer tenantId, final Integer promotionId,  final String userName, Map<String,String> input){
		final BunosResult result = new BunosResult();
		final Object obj = processingMap.putIfAbsent(userName, holder);
		if(obj == null){
			try {
					return giveMeBonusSafe(tenantId,promotionId, userName, input, result);
			} finally{
					processingMap.remove(userName);
			}
		}else{
			result.setCode(BunosResult.BUSY);
			return result;
		}
			
	}


	/**
	 * @param userName
	 * @param promotionId
	 * @param input 
	 * @param result
	 * @return
	 */
	protected BunosResult giveMeBonusSafe(final Integer tenantId, final Integer promotionId, final String userName,
			 Map<String, String> input, final BunosResult result) {
		final Map<String,Object> context = new HashMap<String,Object>();
		readonlyTransaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Promotion promotion = promotionDAO.get(promotionId);
				if(promotion == null || promotion.getTenantId() != tenantId){
					return;
				}
				context.put(PROMOTION, promotion);
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("promotionId", promotionId);
				PromotionBalance promotionBalance = promotionBalanceDAO.find("promotionBalance.byPromotion", parameters);
				if(promotionBalance == null){
					return;
				}
				context.put(PROMOTION_BALANCE, promotionBalance);
				parameters.clear();
				parameters.put("name", userName);
				parameters.put("tenantId", tenantId);
				User user = userDAO.find("user.byName", parameters);
				if(user == null){
					return;
				}
				context.put(USER, user);
				parameters.clear();
				parameters.put("promotionId", promotionId);
				parameters.put("userId", user.getId());
				List<Bonus> bonuses = bonusDAO.query("bonus.byUser", parameters);
				context.put(GRANTED, bonuses);
			}
		});
		
		Promotion promotion = (Promotion) context.get(PROMOTION);
		if(promotion==null){
			result.setCode(BunosResult.INVALID_PROMOTION);
			return result;
		}
		PromotionBalance promotionBalance = (PromotionBalance) context.get(PROMOTION_BALANCE);
		if(promotionBalance==null){
			result.setCode(BunosResult.INVALID_PROMOTION);
			return result;
		}
		if(promotionBalance.getBalance()<=0){
			result.setCode(BunosResult.PROMOTION_USEDUP);
			return result;			
		}
		User user = (User) context.get(USER);
		if(user == null){
			user = createNewUser(userName,tenantId);
			context.put(USER, user);
			context.put(ISNEWUSER, Boolean.TRUE);
			context.put(GRANTED, Collections.EMPTY_LIST);
		}else{
			context.put(ISNEWUSER, Boolean.FALSE);
		}
		context.put(PARAMETERS, input);
		Long bonusSize = promotionPolicy.evaluate(promotion, context);
		if(bonusSize != null&& bonusSize>0){
		
			Bonus bonus = grantBonus(promotion,promotionBalance,user, bonusSize);
			if(bonus != null){
				result.setBonus(bonus);
				result.setCode(BunosResult.SUCCESS);
			}
		}else{
			result.setCode(BunosResult.UNLUCKY);
		}
		return result;
	}


	/**
	 * @param promotion
	 * @param promotionBalance
	 * @param user
	 * @param bonusSize
	 * @return
	 */
	private Bonus grantBonus(Promotion promotion,
			final PromotionBalance promotionBalance, User user, final Long bonusSize) {
		final Bonus bonus = new Bonus();
		bonus.setSize(bonusSize);
		bonus.setPromotionId(promotion.getId());
		bonus.setUserId(user.getId());
		bonus.setTargetUserId(user.getId());
		bonus.setTenantId(promotion.getTenantId());
		bonus.setExpirationTime(promotion.getBonusExpirationTime());
		Long id = this.transaction.execute(new TransactionCallback<Long>(){

			@Override
			public Long doInTransaction(TransactionStatus status) {
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("delta", bonusSize);
				parameters.put("id", promotionBalance.getId());
				parameters.put("updateTime", System.currentTimeMillis());
				int success = promotionBalanceDAO.batchUpdate("promotionBalance.debit", parameters);
				if(success != 1){
					return null;
				}
				
				initEntity(bonus);
				return bonusDAO.create(bonus);
			}

			});	
		if(id == null){
			return null;
		}
		bonus.setId(id);
		return bonus;
	}


	private User createNewUser(String userName, Integer tenantId) {
		final User user = new User();
		user.setName(userName);
		user.setTenantId(tenantId);
		Long id = this.transaction.execute(new TransactionCallback<Long>(){

			@Override
			public Long doInTransaction(TransactionStatus status) {
				initEntity(user);
				return userDAO.create(user);
			}

			});
		user.setId(id);
		return user;
	}
	


}
