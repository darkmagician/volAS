/**
 * 
 */
package com.vol.mgmt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.common.user.Bonus;
import com.vol.common.user.Quota;
import com.vol.common.user.User;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class BonusMgmtImpl extends AbstractService {

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
	
	public Bonus getBonus(final Long id) {
		Bonus bonus = this.readonlyTransaction
				.execute(new TransactionCallback<Bonus>() {

					@Override
					public Bonus doInTransaction(TransactionStatus status) {
						return bonusDao.get(id);
					}
				});
		return bonus;
	}

	public List<Bonus> listBonusByUserName(final Integer tenantid,
			final String userName) {
		return this.readonlyTransaction
				.execute(new TransactionCallback<List<Bonus>>() {

					@Override
					public List<Bonus> doInTransaction(TransactionStatus status) {
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("name", userName);
						parameters.put("tenantId", tenantid);
						User user = userDao.find("user.byName", parameters);
						if (user == null) {
							if (log.isDebugEnabled()) {
								log.debug(
										"bonus is not found because User[name={},tenantid={}] is not found",
										userName, tenantid);
							}
							return null;
						}
						parameters.clear();
						parameters.put("userId", user.getId());
						return bonusDao.query("bonus.byUser", parameters);
					}
				});
	}
	
	public List<Bonus> listBonusByUser(
			final Long userId) {
		return this.readonlyTransaction
				.execute(new TransactionCallback<List<Bonus>>() {

					@Override
					public List<Bonus> doInTransaction(TransactionStatus status) {
						Map<String, Object> parameters = Collections.singletonMap("userId", (Object)userId);
						return bonusDao.query("bonus.byUser", parameters);
					}
				});
	}
	
	public boolean transfer(final Integer tenantid, final Long id,
			final String userName){
		
		return this.transaction.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				Bonus bonus = bonusDao.get(id);
				if (bonus == null) {
					if (log.isDebugEnabled()) {
						log.debug("bonus is not found for id={}", id);
					}
					
					return false;
				}
				if(bonus.getTargetQuotaId() > 0){
					if (log.isDebugEnabled()) {
						log.debug("bonus is actived id={} to quota [{}]", id,bonus.getTargetQuotaId());
					}
					return false;
				}
				long now = System.currentTimeMillis();
				if(bonus.getExpirationTime()<now){
					if (log.isDebugEnabled()) {
						log.debug("bonus is expired id={}", id);
					}
					return false;
				}
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("name", userName);
				parameters.put("tenantId", tenantid);
				User user = userDao.find("user.byName", parameters);
				if (user == null) {
					if (log.isDebugEnabled()) {
						log.debug(
								"bonus is not found because User[name={},tenantid={}] is not found",
								userName, tenantid);
					}
					return false;
				} 
				parameters.clear();
				parameters.put("id", bonus.getId());
				parameters.put("current", now);
				parameters.put("currentUserId", bonus.getTargetUserId());
				parameters.put("targetUserId", user.getId());
				int rc=bonusDao.batchUpdate("bonus.transfer", parameters );
				if(rc != 1){
					return false;
				}
				
				return true;
			}
		});
	}

	public boolean active(final Integer tenantid, final Long id,
			final String userName) {
		return this.transaction.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus status) {
				Bonus bonus = bonusDao.get(id);
				if (bonus == null) {
					if (log.isDebugEnabled()) {
						log.debug("bonus is not found for id={}", id);
					}
					
					return false;
				}
				if(bonus.getTargetQuotaId() > 0){
					if (log.isDebugEnabled()) {
						log.debug("bonus is actived id={} to quota [{}]", id,bonus.getTargetQuotaId());
					}
					return false;
				}
				long now = System.currentTimeMillis();
				if(bonus.getExpirationTime()<now){
					if (log.isDebugEnabled()) {
						log.debug("bonus is expired id={}", id);
					}
					return false;
				}
				Map<String, Object> parameters = new HashMap<String, Object>();
				long userId = bonus.getUserId();
				User user=null;
				if (userName != null) {
					parameters.put("name", userName);
					parameters.put("tenantId", tenantid);
					user = userDao.find("user.byName", parameters);
					if (user == null) {
						if (log.isDebugEnabled()) {
							log.debug(
									"bonus is not found because User[name={},tenantid={}] is not found",
									userName, tenantid);
						}
						return null;
					} else {
						userId = user.getId();
					}

				}
				parameters.clear();
				parameters.put("userId", userId);
				parameters.put("volumeType", bonus.getVolumeType());
				Quota quota = quotaDao.find("quota.byUserVolType", parameters);

				
				
				long size = bonus.getSize();
				if (quota == null) {
					if(user == null){
						user = userDao.get(userId);
					}
					// init the quota
					quota = new Quota();
					quota.setUserId(userId);
					quota.setUserName(user.getName());
					quota.setTenantId(user.getTenantId());
					quota.setMaximum(size);
					quota.setBalance(size);
					quota.setVolumeType(bonus.getVolumeType());
					initEntity(quota);
					quota.setExpirationTime(calculateExpirationTime(
							now, tenantid));
					Long quotaId = quotaDao.create(quota);
					boolean success = quotaId != null;
					if(success){
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
					parameters.put("oldExpirationTime",
							quota.getExpirationTime());
					parameters.put(
							"newExpirationTime",
							calculateExpirationTime(now,tenantid));
					int rc = quotaDao.batchUpdate("quota.renew", parameters);
					boolean success = rc==1;
					if(success){
						updateBonusToActivated(bonus, quota.getId(),now);
					}
				}
				// delta update
				parameters.clear();
				parameters.put("id", quota.getId());
				parameters.put("delta", size);
				parameters.put("updateTime", now);
				parameters.put("expirationTime", quota.getExpirationTime());
				int rc = quotaDao.batchUpdate("quota.topup", parameters);
				boolean success = rc==1;
				if(success){
					updateBonusToActivated(bonus, quota.getId(),now);
				}
				return success;

			}



		}

		);
	}
	/**
	 * @param bonus
	 * @param quotaId
	 * @param now 
	 */
	private void updateBonusToActivated(Bonus bonus, Long quotaId, long now) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("id", bonus.getId());
		parameters.put("targetQuotaId", quotaId);
		parameters.put("current", now);
		parameters.put("currentUserId", bonus.getTargetUserId());
		int rc=bonusDao.batchUpdate("bonus.activate", parameters );
		if(rc != 1){
			throw new IllegalStateException("failed to update bonus");
		}
	}
	
	private long calculateExpirationTime(long activationTime, Integer tenantid) {
		Tenant tenant = tenantDao.get(tenantid);
		return cycleHandler.calculateCycleEndTime(activationTime, tenant);
	}
}
