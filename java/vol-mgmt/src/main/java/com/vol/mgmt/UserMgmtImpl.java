/**
 * 
 */
package com.vol.mgmt;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.vol.common.DAO;
import com.vol.common.user.User;
import com.vol.dao.AbstractQueryService;

/**
 * @author scott
 *
 */
public class UserMgmtImpl extends AbstractQueryService<Long,User> {
	@Resource(name = "userDao")
	protected DAO<Long, User> userDao;
	
	
	public User getUserByName(final Integer tenantId, final String name){
		User user = this.readonlyTransaction
				.execute(new TransactionCallback<User>() {

					@Override
					public User doInTransaction(TransactionStatus status) {
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("name", name);
						parameters.put("tenantId", tenantId);
						return userDao.find("user.byName", parameters);
					}
				});
		return user;
	}

	/* (non-Javadoc)
	 * @see com.vol.dao.AbstractQueryService#getDAO()
	 */
	@Override
	protected DAO<Long, User> getDAO() {
		return userDao;
	}
}
