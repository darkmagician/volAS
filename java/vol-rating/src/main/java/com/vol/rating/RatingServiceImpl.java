/**
 * 
 */
package com.vol.rating;

import com.vol.common.service.RatingService;
import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class RatingServiceImpl implements RatingService {

	/* (non-Javadoc)
	 * @see com.vol.common.service.RatingService#rate(com.vol.common.tenant.Tenant, long, long, long)
	 */
	@Override
	public double rate(Tenant tenant, long startDate, long endDate, long usage) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.RatingService#validatePlan(com.vol.common.tenant.Tenant)
	 */
	@Override
	public void validatePlan(Tenant tenant) {
		// TODO Auto-generated method stub

	}

}
