package com.vol.promotion.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.vol.common.tenant.Promotion;
import com.vol.promotion.rule.dt.ColumnDefinition;
import com.vol.promotion.rule.dt.DecisionTableDefinition;

/**
 * @author scott
 * 
 */
public class DecistionTableTest extends BaseTest {

	@Resource(name = "DTPromotionPolicy")
	PromotionPolicySPI promotionPolicySPI;

	@Test
	public void testJSON() {

		Promotion promotion = new Promotion();
		promotion
				.setRule("{\"cols\":[{\"title\":\"1111\",\"src\":\"username\",\"desc\":\"\",\"defaultVal\":\"1\",\"type\":\"N\"}],\"data\":[[\"1\",\"1\"]]}");
		promotionPolicySPI.validate(promotion);

	}

	private final ObjectMapper m = new ObjectMapper();

	@Test
	public void testEvaluate() throws Exception {
		Promotion promotion = new Promotion();
		DecisionTableDefinition def = new DecisionTableDefinition();

		List<ColumnDefinition> cols = new ArrayList<ColumnDefinition>();

		List<String[]> data = new ArrayList<String[]>();
		{
			ColumnDefinition columnDefinition = new ColumnDefinition();
			columnDefinition.setSrc("username");
			columnDefinition.setType(ColumnDefinition.STRING);
			cols.add(columnDefinition);
		}
		{
			ColumnDefinition columnDefinition = new ColumnDefinition();
			columnDefinition.setSrc("totalVolume");
			columnDefinition.setType(ColumnDefinition.NUMBER);
			cols.add(columnDefinition);
		}
		{
			ColumnDefinition columnDefinition = new ColumnDefinition();
			columnDefinition.setSrc("bonus");
			columnDefinition.setType(ColumnDefinition.NUMBER);
			cols.add(columnDefinition);
		}
		data.add(new String[] { "a1", "", "1000" });
		data.add(new String[] { "a2", "", "2000" });
		data.add(new String[] { "", "1000~2000", "3000" });
		data.add(new String[] { "", "3000~4000", "4000" });
		data.add(new String[] { "a5,a6", "0", "5000" });

		def.setCols(cols.toArray(new ColumnDefinition[cols.size()]));
		def.setData(data.toArray(new String[data.size()][]));
		String rule = m.writeValueAsString(def);
		System.out.println(rule);
		promotion.setRule(rule);
		HashMap<String, Object> context = new HashMap<String, Object>();
		context.put("totalVolume", 0L);
		{
			context.put("username", "a1");
			Assert.assertEquals(1000L,
					promotionPolicySPI.evaluate(promotion, context).longValue());
		}
		{
			context.put("username", "a2");
			Assert.assertEquals(2000L,
					promotionPolicySPI.evaluate(promotion, context).longValue());
		}
		{
			context.put("username", "a3");
			context.put("totalVolume", 1000L);
			Assert.assertEquals(3000L,
					promotionPolicySPI.evaluate(promotion, context).longValue());
		}
		{
			context.put("username", "a4");
			context.put("totalVolume", 3500L);
			Assert.assertEquals(4000L,
					promotionPolicySPI.evaluate(promotion, context).longValue());
		}
		{
			context.put("username", "a5");
			context.put("totalVolume", 0L);
			Assert.assertEquals(5000L,
					promotionPolicySPI.evaluate(promotion, context).longValue());
		}
	}
}
