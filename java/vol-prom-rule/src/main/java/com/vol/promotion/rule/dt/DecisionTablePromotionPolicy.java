/**
 * 
 */
package com.vol.promotion.rule.dt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

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

	private static final double MINOR_VALUE = 0.000000000001d;
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
	
	private Random random = new Random();

	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant.Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {
		DecisionTableDefinition definition = toDefinition(promotion.getRule());
		ColumnDefinition[] cols = definition.getCols();
		String[][] data = definition.getData();
		nextRow: for (int i = 0 ; i < data.length; i++) {
			String[] row = data[i];
			
			for (int j = 0; j < cols.length - 1; j++) {
				ColumnDefinition def = cols[j];

				String grid = row[j];
				if (grid == null || grid.isEmpty()) {
					//no condition, means meet
					continue;
				}
				
				Object userValueRaw = context.get(def.getSrc());
				if (ColumnDefinition.NUMBER == def.getType()) {
					double userValue = ((Number) userValueRaw).doubleValue();
					
					if (grid.contains("~")) {
						double low = Double.parseDouble(grid.substring(0, grid.indexOf("~")));
						double high = Double.parseDouble(grid.substring(grid.indexOf("~") + 1));
						
						if (userValue > low - MINOR_VALUE && userValue <= high + MINOR_VALUE) {
							//means meet
						} else {
							continue nextRow;
						}
						
					} else {
						
						double expect = Double.parseDouble(grid);
						if (userValue > expect - MINOR_VALUE && userValue <= expect + MINOR_VALUE) {
							//means meet
						} else {
							continue nextRow;
						}
						
					}
					
				} else if (ColumnDefinition.STRING == def.getType()) {
					String userValue = (String) userValueRaw;
					
					if (grid.contains(",")) {
						if (Arrays.asList(grid.split(",")).contains(userValue)) {
							//means include
						} else {
							//means not match
							continue nextRow;
						}
					} else {
						//? case sensitive
						if (grid.equals(userValue)) {
						} else {
							//not match
							continue nextRow;
						}
					}
				}
			}
			
			// means meet all
			//assume last column is the bonus
			String bonusRaw = row[cols.length - 1];
			if (bonusRaw.contains(",")) {
				String[] split = bonusRaw.split(",");
				return Long.parseLong(split[random.nextInt(split.length)]);
			} else {
				return Long.parseLong(bonusRaw);
			}
			
		}
				
		return 0L;
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
