/**
 * 
 */
package com.vol.promotion.rule.dt;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.vol.common.tenant.Promotion;
import com.vol.promotion.rule.AbstractPromotionPolicy;
import com.vol.promotion.rule.PromotionPolicySPI;

/**
 * @author scott
 *
 */
public class DecisionTablePromotionPolicy  extends AbstractPromotionPolicy implements PromotionPolicySPI {

	private final ObjectMapper m = new ObjectMapper();
	
	
	/**
	 * @param json
	 * @return
	 */
	private DecisionTableDefinition toDefinition(String json){
		try {
			return m.readValue(json, DecisionTableDefinition.class);
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
	 * @see com.vol.common.service.PromotionPolicy#validate(com.vol.common.tenant.Promotion)
	 */
	@Override
	public void validate(Promotion promotion) {
		DecisionTableDefinition definition = toDefinition(promotion.getRule());
		log.info("Validating: {}",definition);
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant.Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#precompile(com.vol.common.tenant.Promotion)
	 */
	@Override
	public void precompile(Promotion promotion) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.vol.promotion.rule.PromotionPolicySPI#getType()
	 */
	@Override
	public short getType() {
		return DECISION_TABLE;
	}

}
