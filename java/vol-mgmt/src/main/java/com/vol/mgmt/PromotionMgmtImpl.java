/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;
import com.vol.common.mgmt.VolMgmtException;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.PromotionBalance;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class PromotionMgmtImpl extends AbstractService{
	
	
	@Resource(name="promotionDao")
	protected DAO<Integer,Promotion> promotionDAO;
	@Resource(name="promotionBalanceDao")
	protected DAO<Integer,PromotionBalance> promotionBalanceDAO;
	
	public Integer addPromotion(final Promotion promotion){
		if(log.isDebugEnabled()){
			log.debug("adding promotion {}", promotion);
		}
		validatePromotion(promotion);
		Integer id = this.transaction.execute(new TransactionCallback<Integer>(){

			@Override
			public Integer doInTransaction(TransactionStatus status) {
				initEntity(promotion);
				promotion.setStatus(BaseEntity.DRAFT);// init draft status
				return promotionDAO.create(promotion);
			}

			});
		if(log.isDebugEnabled()){
			log.debug("promotion id={} is added", id);
		}		
		return id;
	}
	
	private void validatePromotion(Promotion promotion) {
		//if(promo)
		
	}
	
	public void updatePromotion(final Promotion promotion){
		validatePromotion(promotion);
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Promotion oldPromotion = promotionDAO.get(promotion.getId());
				if(oldPromotion.getStatus() != BaseEntity.DRAFT){
					log.error("Promotion {} cannot be modified if it is not in draft.",oldPromotion);
					throw new VolMgmtException("The record cannot be modified if it is not in draft.");
				}	
				validateUpdateTime(promotion, oldPromotion);
				copyAttribute(promotion,oldPromotion);
				updateEntity(oldPromotion);
				promotionDAO.update(oldPromotion);
				
			}

		});
	}
	private void copyAttribute(Promotion promotion,
			Promotion oldPromotion) {
		oldPromotion.setDescription(promotion.getDescription());
		oldPromotion.setStartTime(promotion.getStartTime());
		oldPromotion.setEndTime(promotion.getEndTime());
		oldPromotion.setName(promotion.getName());
		oldPromotion.setRule(promotion.getRule());
	}
	
	
	public Promotion getPromotion(final Integer id){
		Promotion promotion = this.readonlyTransaction.execute(new TransactionCallback<Promotion>(){

			@Override
			public Promotion doInTransaction(TransactionStatus status) {
				return promotionDAO.get(id);
			}});	
		return promotion;
	}
	
	public List<Promotion> list(final String queryName, final Map<String,Object> parameters){
		return this.readonlyTransaction.execute(new TransactionCallback<List<Promotion>>(){

			@Override
			public List<Promotion> doInTransaction(TransactionStatus status) {
				return promotionDAO.query(queryName, parameters);
			}});	
	}
	
	public void activate(final int id) {
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Promotion oldPromotion = promotionDAO.get(id);
				if(oldPromotion.getStatus() != BaseEntity.DRAFT){
					log.error("Promotion {} cannot be modified if it is not in draft.",oldPromotion);
					throw new VolMgmtException("The record cannot be modified if it is not in draft.");
				}	
				updateEntity(oldPromotion);
				promotionDAO.update(oldPromotion);
				PromotionBalance promotionBalance = createBalance(oldPromotion,
						id);
				initEntity(promotionBalance);
				promotionBalanceDAO.create(promotionBalance);
				
			}



		});
	}
	
	/**
	 * @param oldPromotion
	 * @param promotionId
	 * @return
	 */
	private PromotionBalance createBalance(Promotion oldPromotion,
			final int promotionId) {
		PromotionBalance promotionBalance=new PromotionBalance();
		promotionBalance.setMax(oldPromotion.getMax());
		promotionBalance.setPromotionId(promotionId);
		promotionBalance.setBalance(oldPromotion.getMax());
		return promotionBalance;
	}
	
	public PromotionBalance getPromotionBalance(final int id){
		PromotionBalance promotionBalance = this.readonlyTransaction.execute(new TransactionCallback<PromotionBalance>(){

			@Override
			public PromotionBalance doInTransaction(TransactionStatus status) {
				return promotionBalanceDAO.get(id);
			}});	
		return promotionBalance;		
	}
	
	public List<PromotionBalance> getPromotionBalanceByPromotion(final Integer promotionId){
		final Map<String, Object> parameters=Collections.singletonMap("promotionId", (Object)promotionId);
		return this.readonlyTransaction.execute(new TransactionCallback<List<PromotionBalance>>(){

			@Override
			public List<PromotionBalance> doInTransaction(TransactionStatus status) {
	
				return promotionBalanceDAO.query("PromotionBalance.byPromotionId", parameters);
			}});			
	}
}
