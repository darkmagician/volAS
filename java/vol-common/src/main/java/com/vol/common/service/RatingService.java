/**
 * 
 */
package com.vol.common.service;

import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public interface RatingService {

	/**
	 * @param tenant
	 * @param startDate
	 * @param endDate
	 * @param usage
	 * @return
	 */
	public double rate(Tenant tenant, long startDate, long endDate, long usage);
	
	
	
	/**
	 * @param tenant
	 */
	public void validatePlan(Tenant tenant);
}
