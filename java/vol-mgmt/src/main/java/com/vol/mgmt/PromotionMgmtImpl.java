/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vol.common.BaseEntity;
import com.vol.common.DAO;
import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.mgmt.PagingResult;
import com.vol.common.service.PromotionPolicy;
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

	@Resource(name = "promotionPolicyService")
	protected PromotionPolicy promotionPolicy;

	protected void copyAttribute(Promotion promotion, Promotion oldPromotion) {
		oldPromotion.setDescription(promotion.getDescription());
		oldPromotion.setStartTime(promotion.getStartTime());
		oldPromotion.setEndTime(promotion.getEndTime());
		oldPromotion.setBonusExpirationTime(promotion.getBonusExpirationTime());
		oldPromotion.setName(promotion.getName());
		oldPromotion.setRule(promotion.getRule());
		oldPromotion.setMaximum(promotion.getMaximum());
	}

	protected short getInitState() {
		return BaseEntity.DRAFT;
	}

	public void activate(final Integer tenantId, final Integer id) {
		try {
			txBegin();
			Promotion oldPromotion = promotionDAO.get(id);
			if (oldPromotion.getStatus() != BaseEntity.DRAFT) {
				throw new MgmtException(ErrorCode.MODIFICATION_NOT_ALLOW,
						"The promotion is not in draft. It cannot be modified. ID="
								+ oldPromotion.getId());
			}
			oldPromotion.setStatus(BaseEntity.ACTIVE);
			updateEntity(oldPromotion);
			promotionDAO.update(oldPromotion);
			PromotionBalance promotionBalance = createBalance(oldPromotion, id);
			initEntity(promotionBalance);
			promotionBalanceDAO.create(promotionBalance);

			txCommit();
		} catch (Exception e) {
			txRollback(e);
		}
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
		try {
			txBeginRO();
			PromotionBalance result = promotionBalanceDAO.get(id);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
	}

	public List<PromotionBalance> getPromotionBalanceByPromotion(
			final Integer promotionId) {
		final Map<String, Object> parameters = Collections.singletonMap(
				"promotionId", (Object) promotionId);
		try {
			txBeginRO();

			List<PromotionBalance> result = promotionBalanceDAO.query(
					"promotionBalance.byPromotion", parameters);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vol.dao.AbstractService#validateToBeDelete(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validateToBeDelete(Promotion old) {
		if (old.getStatus() != BaseEntity.DRAFT) {
			throw new MgmtException(ErrorCode.DELETE_NOT_ALLOW,
					"The promotion is not in draft. It cannot be deleted. ID="
							+ old.getId());
		}
	}

	public PagingResult<Promotion> searchHistory(
			final Map<String, Object> parameters, final int startPage,
			final int pageSize) {
		StringBuilder sb = new StringBuilder();

		sb.append("from Promotion where tenantId=:tenantId ");

		Object fromTime = parameters.get("fromTime");
		if (fromTime != null) {
			sb.append(" AND endTime>:fromTime ");
		}
		Object toTime = parameters.get("toTime");
		if (toTime != null) {
			sb.append(" AND startTime<:toTime ");
		}
		String name = (String) parameters.get("name");
		if (name != null && !name.isEmpty()) {
			sb.append(" AND name like '%'||:name||'%' ");
		}
		final String hql = sb.toString();
		try {
			txBeginRO();
			PagingResult<Promotion> result = getDAO().queryByPageUsingHQL(hql,
					parameters, startPage, pageSize);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#validate(com.vol.common.BaseEntity)
	 */
	@Override
	protected void validate(Promotion obj) {
		validateNotNull(obj);
		validateNotEmpty(obj, "name", obj.getName());
		validateNotEmpty(obj, "rule", obj.getRule());
		validateDate(obj, "startTime", obj.getStartTime());
		validateDate(obj, "endTime", obj.getEndTime());
		validateDate(obj, "bonusExpirationTime", obj.getBonusExpirationTime());
		if (obj.getStartTime() >= obj.getEndTime()) {
			throw new MgmtException(ErrorCode.START_LATER_THAN_END, "START "
					+ new Date(obj.getStartTime()) + " is later than END "
					+ new Date(obj.getEndTime()));
		}
		if (obj.getBonusExpirationTime() < obj.getEndTime()) {
			throw new MgmtException(ErrorCode.END_LATER_THAN_EXPIRATION, "END "
					+ new Date(obj.getStartTime())
					+ " is later than EXPIRATION " + new Date(obj.getEndTime()));
		}

		promotionPolicy.validate(obj);
	}

}
