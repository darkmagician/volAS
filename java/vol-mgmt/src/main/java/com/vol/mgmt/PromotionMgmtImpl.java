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
public class PromotionMgmtImpl extends AbstractService<Integer, Promotion> {

	@Resource(name = "promotionDao")
	protected DAO<Integer, Promotion> promotionDAO;
	@Resource(name = "promotionBalanceDao")
	protected DAO<Integer, PromotionBalance> promotionBalanceDAO;

	protected void copyAttribute(Promotion promotion, Promotion oldPromotion) {
		oldPromotion.setDescription(promotion.getDescription());
		oldPromotion.setStartTime(promotion.getStartTime());
		oldPromotion.setEndTime(promotion.getEndTime());
		oldPromotion.setName(promotion.getName());
		oldPromotion.setRule(promotion.getRule());
	}

	
	
	
	protected short getInitState(){
		return BaseEntity.DRAFT;
	}
	
	
	public void activate(final Integer id) {
		this.transaction.execute(new TransactionCallbackWithoutResult() {

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Promotion oldPromotion = promotionDAO.get(id);
				if (oldPromotion.getStatus() != BaseEntity.DRAFT) {
					log.error(
							"Promotion {} cannot be modified if it is not in draft.",
							oldPromotion);
					throw new VolMgmtException(
							"The record cannot be modified if it is not in draft.");
				}
				oldPromotion.setStatus(BaseEntity.ACTIVE);
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
		PromotionBalance promotionBalance = new PromotionBalance();
		promotionBalance.setMaximum(oldPromotion.getMaximum());
		promotionBalance.setPromotionId(promotionId);
		promotionBalance.setBalance(oldPromotion.getMaximum());
		return promotionBalance;
	}

	public PromotionBalance getPromotionBalance(final int id) {
		PromotionBalance promotionBalance = this.readonlyTransaction
				.execute(new TransactionCallback<PromotionBalance>() {

					@Override
					public PromotionBalance doInTransaction(
							TransactionStatus status) {
						return promotionBalanceDAO.get(id);
					}
				});
		return promotionBalance;
	}

	public List<PromotionBalance> getPromotionBalanceByPromotion(
			final Integer promotionId) {
		final Map<String, Object> parameters = Collections.singletonMap(
				"promotionId", (Object) promotionId);
		return this.readonlyTransaction
				.execute(new TransactionCallback<List<PromotionBalance>>() {

					@Override
					public List<PromotionBalance> doInTransaction(
							TransactionStatus status) {

						return promotionBalanceDAO.query(
								"PromotionBalance.byPromotionId", parameters);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Integer, Promotion> getDAO() {
		return promotionDAO;
	}




	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractService#validateToBeDelete(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validateToBeDelete(Promotion old) {
		if(old.getStatus() != BaseEntity.DRAFT){
			throw new IllegalStateException("the promotion is not in draft. It cannot be deleted");
		}
	}
}
