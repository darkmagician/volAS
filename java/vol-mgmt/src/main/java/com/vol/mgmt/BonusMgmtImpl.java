/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.mgmt.PagingResult;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.Tenant;
import com.vol.common.user.Bonus;
import com.vol.common.user.Quota;
import com.vol.common.user.User;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public class BonusMgmtImpl extends AbstractQueryService<Long, Bonus> {

	@Resource(name = "userDao")
	protected DAO<Long, User> userDao;

	@Resource(name = "bonusDao")
	protected DAO<Long, Bonus> bonusDao;

	@Resource(name = "quotaDao")
	protected DAO<Long, Quota> quotaDao;

	@Resource(name = "tenantDao")
	protected DAO<Integer, Tenant> tenantDao;

	@Resource(name = "cycleHandler")
	protected CycleHandler cycleHandler;

	@Resource(name = "promotionDao")
	protected DAO<Integer, Promotion> promotionDAO;

	private final PostProcess<Bonus> bonusPostProcessor = new PostProcess<Bonus>() {

		@Override
		public void process(PagingResult<Bonus> result) {
			List<Bonus> rows = result.getRows();
			if (rows != null) {
				Map<Long, String> users = new HashMap<Long, String>();
				Map<Integer, String> promotions = new HashMap<Integer, String>();
				for (Bonus bonus : rows) {
					long userId = bonus.getUserId();
					String userName = users.get(userId);
					if (userName == null) {
						User user = userDao.get(userId);
						userName = user.getName();
						users.put(userId, userName);
					}
					bonus.setUserName(userName);

					int promotionId = bonus.getPromotionId();
					String promotionName = promotions.get(promotionId);
					if (promotionName == null) {
						Promotion promotion = promotionDAO.get(promotionId);
						promotionName = promotion.getName();
						promotions.put(promotionId, promotionName);
					}
					bonus.setPromotionName(promotionName);
				}
			}
		}

	};

	public List<Bonus> listBonusByUserName(final Integer tenantid,
			final String userName) {
		try {
			txBeginRO();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("targetUserName", userName);
			parameters.put("tenantId", tenantid);
			List<Bonus> result = bonusDao
					.query("bonus.byOwnedName", parameters);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
	}

	public List<Bonus> listBonusByUser(final Long userId) {
		try {
			txBeginRO();
			Map<String, Object> parameters = Collections.singletonMap("userId",
					(Object) userId);
			List<Bonus> result = bonusDao.query("bonus.byUser", parameters);
			txCommit();
			return result;
		} catch (Exception e) {
			txRollback(e);
			return null;
		}
	}

	public boolean transfer(final Integer tenantid, final Long id,
			final String fromUserName, final String toUser) {

		try {
			txBegin();
			Bonus bonus = bonusDao.get(id);
			if (bonus == null) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is not found for id={}", id);
				}

				return false;
			}
			if (bonus.getTargetQuotaId() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is actived id={} to quota [{}]", id,
							bonus.getTargetQuotaId());
				}
				return false;
			}
			long now = System.currentTimeMillis();
			if (bonus.getExpirationTime() < now) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is expired id={}", id);
				}
				return false;
			}
			if (!fromUserName.equals(bonus.getTargetUserName())) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is not owned by userName={}", fromUserName);
				}
				return false;
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("id", bonus.getId());
			parameters.put("current", now);
			parameters.put("targetUserName", fromUserName);
			parameters.put("newTargetUserName", toUser);
			int rc = bonusDao.batchUpdate("bonus.transfer", parameters);
			if (rc != 1) {
				txRollback();
				return false;
			}
			txCommit();
			return true;

		} catch (Exception e) {
			txRollback(e);
			return false;
		}
	}

	public boolean active(final Integer tenantid, final Long id,
			final String username) {
		try {
			txBegin();
			Bonus bonus = bonusDao.get(id);
			if (bonus == null) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is not found for id={}", id);
				}
				txRollback();
				return false;
			}
			if (bonus.getTenantId() != tenantid) {
				if (log.isDebugEnabled()) {
					log.debug(
							"TenantId is not matched [current={}, bonusTenant={}]",
							tenantid, bonus.getTenantId());
				}
				txRollback();
				return false;
			}
			if (bonus.getTargetQuotaId() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is actived id={} to quota [{}]", id,
							bonus.getTargetQuotaId());
				}
				txRollback();
				return false;
			}
			long now = System.currentTimeMillis();
			if (bonus.getExpirationTime() < now) {
				if (log.isDebugEnabled()) {
					log.debug("bonus is expired id={}", id);
				}
				txRollback();
				return false;
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			String userName = bonus.getTargetUserName();

			parameters.put("tenantId", tenantid);
			parameters.put("userName", userName);
			parameters.put("volumeType", bonus.getVolumeType());
			Quota quota = quotaDao.find("quota.byUserVolType", parameters);

			long size = bonus.getSize();
			if (quota == null) {
				User user = findUser(userName, tenantid);
				// init the quota
				quota = new Quota();
				quota.setUserId(user.getId());
				quota.setUserName(user.getName());
				quota.setTenantId(user.getTenantId());
				quota.setMaximum(size);
				quota.setBalance(size);
				quota.setVolumeType(bonus.getVolumeType());
				initEntity(quota);
				quota.setExpirationTime(calculateExpirationTime(now, tenantid));
				quota.setActivationTime(now);
				Long quotaId = quotaDao.create(quota);
				boolean success = quotaId != null;
				if (success) {
					updateBonusToActivated(bonus, quotaId, now);
				}
				return success;
			}

			if (quota.getExpirationTime() <= now) {
				// expired
				parameters.clear();
				parameters.put("id", quota.getId());
				parameters.put("initial", size);
				parameters.put("updateTime", now);
				parameters.put("oldExpirationTime", quota.getExpirationTime());
				parameters.put("newExpirationTime",
						calculateExpirationTime(now, tenantid));
				int rc = quotaDao.batchUpdate("quota.renew", parameters);
				boolean success = rc == 1;
				if (success) {
					updateBonusToActivated(bonus, quota.getId(), now);
				}
			}
			// delta update
			parameters.clear();
			parameters.put("id", quota.getId());
			parameters.put("delta", size);
			parameters.put("updateTime", now);
			parameters.put("expirationTime", quota.getExpirationTime());
			int rc = quotaDao.batchUpdate("quota.topup", parameters);
			boolean success = rc == 1;
			if (success) {
				updateBonusToActivated(bonus, quota.getId(), now);
			} else {
				txRollback();
			}
			return success;
		} catch (Exception e) {
			txRollback(e);
			return false;
		} finally {
			txCommit();
		}
	}

	private User findUser(String userName, Integer tenantId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", userName);
		parameters.put("tenantId", tenantId);
		User user = userDao.find("user.byName", parameters);
		if (user == null) {
			user = new User();
			user.setName(userName);
			user.setTenantId(tenantId);
			initEntity(user);
			userDao.create(user);
		}
		return user;
	}

	/**
	 * @param bonus
	 * @param quotaId
	 * @param now
	 */
	private void updateBonusToActivated(Bonus bonus, Long quotaId, long now) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", bonus.getId());
		parameters.put("targetQuotaId", quotaId);
		parameters.put("current", now);
		parameters.put("targetUserName", bonus.getTargetUserName());
		int rc = bonusDao.batchUpdate("bonus.activate", parameters);
		if (rc != 1) {
			throw new IllegalStateException("failed to update bonus");
		}
	}

	private long calculateExpirationTime(long activationTime, Integer tenantid) {
		Tenant tenant = tenantDao.get(tenantid);
		return cycleHandler.calculateCycleEndTime(activationTime, tenant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractService#getDAO()
	 */
	@Override
	protected DAO<Long, Bonus> getDAO() {
		return bonusDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.dao.AbstractQueryService#listByPaging(java.lang.String,
	 * java.util.Map, int, int)
	 */
	@Override
	public PagingResult<Bonus> listByPaging(String queryName,
			Map<String, Object> parameters, int startPage, int pageSize) {
		return super.listByPaging(queryName, parameters, startPage, pageSize,
				bonusPostProcessor);
	}

}
