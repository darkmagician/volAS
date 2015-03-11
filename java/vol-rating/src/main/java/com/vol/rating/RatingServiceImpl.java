/**
 * 
 */
package com.vol.rating;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.vol.common.service.RatingService;
import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class RatingServiceImpl implements RatingService {
	private final ObjectMapper m = new ObjectMapper();
	/**
	 * @param json
	 * @return
	 */
	private RatingPlan toDefinition(String json){
		try {
			return m.readValue(json, RatingPlan.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
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
		String planContent = tenant.getRatingPlan();
		RatingPlan plan = toDefinition(planContent);

	}

}
