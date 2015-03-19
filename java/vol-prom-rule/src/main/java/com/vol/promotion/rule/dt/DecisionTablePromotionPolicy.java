/**
 * 
 */
package com.vol.promotion.rule.dt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.vol.common.tenant.Promotion;
import com.vol.promotion.rule.AbstractPromotionPolicy;
import com.vol.promotion.rule.PromotionPolicySPI;
import com.vol.promotion.rule.dt.DecisionTableDefinition.Checker;

/**
 * @author scott
 * 
 */
public class DecisionTablePromotionPolicy extends AbstractPromotionPolicy
		implements PromotionPolicySPI {

	private static final double MINOR_VALUE = 0.000000000001d;
	private final ObjectMapper m = new ObjectMapper();

	/**
	 * @param json
	 * @return
	 */
	private DecisionTableDefinition toDefinition(String json) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vol.common.service.PromotionPolicy#validate(com.vol.common.tenant
	 * .Promotion)
	 */
	@Override
	public void validate(Promotion promotion) {
		precompile(promotion);
	}

	private Random random = new Random();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant
	 * .Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {

		DecisionTableDefinition definition = (DecisionTableDefinition) promotion
				.getCompiled();
		ColumnDefinition[] cols = definition.getCols();
		String[][] data = definition.getData();

		nextRow: for (int i = 0; i < data.length; i++) {

			String[] row = data[i];

			for (int j = 0; j < cols.length; j++) {

				ColumnDefinition def = cols[j];

				String grid = row[j];
				if (grid == null || grid.isEmpty()) {
					// no condition, means meet
					continue;
				}

				Checker checker = definition.getChecker()[i][j];

				if (checker == null) {
					throw new RuntimeException("not compiled?!");
				}

				Object userValueRaw = context.get(def.getSrc());
				Object userValue = null;

				if (userValueRaw != null) {
					if (ColumnDefinition.NUMBER == def.getType()) {
						userValue = ((Number) userValueRaw).doubleValue();
					} else if (ColumnDefinition.STRING == def.getType()) {
						userValue = (String) userValueRaw;
					}
				} else {
					userValue = null;
				}

				if (checker.ok(userValue)) {
					// means include
				} else {
					// means not match
					continue nextRow;
				}

			}

			// means meet all
			// assume last column is the bonus
			String bonusRaw = row[cols.length];
			if (bonusRaw.contains(",")) {
				String[] split = bonusRaw.split(",");
				return Long.parseLong(split[random.nextInt(split.length)]);
			} else {
				return Long.parseLong(bonusRaw);
			}

		}

		return 0L;
	}

	private boolean isEquals(double real, double expect) {
		return real > expect - MINOR_VALUE && real <= expect + MINOR_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vol.common.service.PromotionPolicy#precompile(com.vol.common.tenant
	 * .Promotion)
	 */
	@Override
	public void precompile(Promotion promotion) {

		DecisionTableDefinition definition = toDefinition(promotion.getRule());
		promotion.setCompiled(definition);

		ColumnDefinition[] cols = definition.getCols();
		String[][] data = definition.getData();

		Checker[][] checkers = new Checker[data.length][];
		definition.setChecker(checkers);

		for (int i = 0; i < data.length; i++) {

			String[] row = data[i];
			checkers[i] = new Checker[cols.length];

			for (int j = 0; j < cols.length; j++) {

				final ColumnDefinition def = cols[j];

				final String grid = row[j];
				Checker checker = null;

				try {

					if (grid == null || grid.isEmpty()) {
						// no condition, means meet
						checker = new Checker() {
							@Override
							public boolean ok(Object value) {
								return true;
							}
						};
					} else if (ColumnDefinition.NUMBER == def.getType()) {

						if (grid.contains("~")) {

							checker = new Checker<Double>() {

								double low = Double.parseDouble(grid.substring(
										0, grid.indexOf("~")));
								double high = Double.parseDouble(grid
										.substring(grid.indexOf("~") + 1));

								public boolean ok(Double userValue) {
									return userValue > low - MINOR_VALUE
											&& userValue <= high + MINOR_VALUE;
								}

							};

						} else if (grid.contains(",")) {

							checker = new Checker<Double>() {
								String[] split = grid.split(",");
								List<Double> doubles = new ArrayList<Double>();

								{
									for (String s : split) {
										doubles.add(Double.parseDouble(s));
									}
									Collections.sort(doubles);
								}

								public boolean ok(Double userValue) {
									return Collections.binarySearch(doubles,
											userValue) >= 0;
								}

							};

						} else {

							checker = new Checker<Double>() {
								double expect = Double.parseDouble(grid);

								public boolean ok(Double userValue) {
									return isEquals(userValue, expect);
								}
							};

						}

					} else if (ColumnDefinition.STRING == def.getType()) {

						if (grid.contains(",")) {
							checker = new Checker<String>() {
								List<String> list = Arrays.asList(grid
										.split(","));

								public boolean ok(String userValue) {
									return list.contains(userValue);
								}
							};
						} else {
							checker = new Checker<String>() {
								String mygrid = grid;

								public boolean ok(String userValue) {
									// ? case sensitive
									return mygrid.equalsIgnoreCase(userValue);
								}
							};
						}
					}

					checkers[i][j] = checker;

				} catch (Exception e) {
					throw new RuntimeException("not acceptable grid, [" + i + "," + j + "]:"
							+ grid);
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vol.promotion.rule.PromotionPolicySPI#getType()
	 */
	@Override
	public short getType() {
		return DECISION_TABLE;
	}

}
