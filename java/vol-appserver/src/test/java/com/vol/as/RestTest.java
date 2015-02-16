/**
 * 
 */
package com.vol.as;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.FormEncodingProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.Test;

import com.vol.common.tenant.Operator;
import com.vol.common.tenant.Promotion;
import com.vol.common.tenant.PromotionBalance;
import com.vol.common.tenant.Tenant;
import com.vol.common.user.Bonus;
import com.vol.common.user.Quota;
import com.vol.common.user.User;
import com.vol.rest.result.BunosResult;
import com.vol.rest.result.OperationResult;
import com.vol.rest.result.PutOperationResult;

/**
 * @author scott
 *
 */
public class RestTest {

	private final String json = "application/json";
	private final String form = "application/x-www-form-urlencoded";

	@Test
	public void test(){
		Response restResult;
		
		Tenant tanent = new Tenant();
		tanent.setName("Tenant1");
		tanent.setDescription("first Tenant");
		
		Operator operator = new Operator();
		operator.setName("operator1");
		operator.setPassword("passwor22d");
		
		Promotion promotion = new Promotion();
		promotion.setBonusExpirationTime(System.currentTimeMillis()+3*24*60*60*1000);
		promotion.setStartTime(System.currentTimeMillis());
		promotion.setEndTime(System.currentTimeMillis()+2*24*60*60*1000);
		promotion.setDescription("Promotion Test1");
		promotion.setName("Promotion1");
		promotion.setMaximum(200000);
		promotion.setRule("return 1000;");
		
		JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
		FormEncodingProvider formProvider = new FormEncodingProvider(true);
		List providers = new ArrayList();
		providers.add(jsonProvider);
		providers.add(formProvider);
		
		WebClient client = WebClient.create("http://127.0.0.1:8080/vol-appserver/admin",providers);
		WebClient.getConfig(client).getInInterceptors().add(new LoggingInInterceptor());
		WebClient.getConfig(client).getOutInterceptors().add(new LoggingOutInterceptor()); 
		
		// create tenant
		System.out.println("creating Tenant");
		client.path("tenant");
		client.type(json).accept(json);
		
		restResult = client.put(tanent);
		PutOperationResult result = restResult.readEntity(PutOperationResult.class);
		System.out.println("put tenant "+result);

		
		client.reset();
		System.out.println("check Tenant");
		int tenantId = 1;//(int) result.getId();
		client.path("tenant/"+tenantId);
		restResult = client.get();
		Tenant tenant1 = restResult.readEntity(Tenant.class);
		System.out.println("get tenant:"+tenant1);
		
		client.reset();
		
		// create operator
		System.out.println("creating Operator");
		client.path("operator");
		client.type(json).accept(json);
		
		operator.setTenantId(tenantId);
		restResult = client.put(operator);
		PutOperationResult result2 = restResult.readEntity(PutOperationResult.class);
		System.out.println("put operator "+result2);
		
		client.reset();
		System.out.println("Check Operator");
		int operatorid = (int) result2.getId();
		client.path("operator/"+operatorid);
		restResult = client.get();
		Operator operator1 = restResult.readEntity(Operator.class);
		System.out.println("get operator:"+operator1);

		client.reset();
		
		//create promotion
		System.out.println("creating promotion");
		client.path("promotion");
		client.type(json).accept(json);
		
		promotion.setTenantId(tenantId);
		promotion.setLastUpdateOperator(operatorid);
		restResult = client.put(promotion);
		PutOperationResult result3 = restResult.readEntity(PutOperationResult.class);
		System.out.println("put promotion "+result3);
		
		client.reset();
		
		System.out.println("get  promotion");
		int promotionId = (int) result3.getId();
		client.path("promotion/"+tenantId+"/"+promotionId);
		client.type(json).accept(json);
		restResult = client.get();
		Promotion promotion1 = restResult.readEntity(Promotion.class);
		System.out.println("get promotion:"+promotion1);	
		
		client.reset();
		
		System.out.println("active  promotion");
		//active promotion
		client.path("promotion/active/"+promotionId);
		client.type(json).accept(json);
		restResult = client.post(null);
		OperationResult result4 = restResult.readEntity(OperationResult.class);
		System.out.println("active promotion:"+result4);
		client.reset();
		
		// public service 
		WebClient client2 = WebClient.create("http://127.0.0.1:8080/vol-appserver/public",providers);
		WebClient.getConfig(client2).getInInterceptors().add(new LoggingInInterceptor());
		WebClient.getConfig(client2).getOutInterceptors().add(new LoggingOutInterceptor());
		
		System.out.println("list  promotion from public");
		client2.path("promotion/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Promotion>> promotionListType = new GenericType<List<Promotion>>(){};
		List<Promotion> promotions = restResult.readEntity(promotionListType);
		System.out.println("list promotion:"+promotions);	
		
		client2.reset();
		
		//grab
		System.out.println("Grab .....");
		client2.path("getbonus/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.post("promotionId="+promotionId+"&userName=User1111");
		BunosResult br = restResult.readEntity(BunosResult.class);
		System.out.println("get bonusResult :"+br);	
		
		client2.reset();
		//get user info
		System.out.println("get User info");
		client2.path("user/"+tenantId);
		client2.type(json).accept(json);
		client2.query("userName", "User1111");
		restResult = client2.get();
		User usr = restResult.readEntity(User.class);
		System.out.println("get user info :"+usr);		
		
		
		client2.reset();
		
		long userId = usr.getId();
		System.out.println("check user's bonus");
		//check bonus
		client2.path("bonus/"+tenantId+"/"+userId);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Bonus>> bonusListType = new GenericType<List<Bonus>>(){};
		List<Bonus> bonuses = restResult.readEntity(bonusListType);
		System.out.println("list bonuses:"+bonuses);
		long bonusId = bonuses.get(bonuses.size()-1).getId();

		client2.reset();
		
		//check promotion balance
		System.out.println("check promotionbalance");
		client.path("promotionbalance");
		client.type(json).accept(json);
		client.query("promotionId", promotionId);
		restResult = client.get();
		PromotionBalance balance = restResult.readEntity(PromotionBalance.class);
		System.out.println("get promotion balance:"+balance);			
		
		client.reset();
		
		System.out.println("active bonus");
		client2.path("activebonus/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.post("bonusId="+bonusId);
		boolean b = restResult.readEntity(Boolean.class);
		System.out.println("get active bonues :"+b);	
		
		client2.reset();
		System.out.println("check activated bonus");
		//check bonus
		client2.path("bonus/"+tenantId+"/"+userId+"/"+bonusId);
		client2.type(form).accept(json);
		restResult = client2.get();
		Bonus bonus = restResult.readEntity(Bonus.class);
		System.out.println("get bonus:"+bonus);
		
		client2.reset();
		//check quota
		System.out.println("check quota");
		client2.path("quota/"+tenantId+"/"+userId);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Quota>> quotalistType = new GenericType<List<Quota>>(){};
		List<Quota> quota = restResult.readEntity(quotalistType);
		System.out.println("get quotas:"+quota);
		
	}
}
