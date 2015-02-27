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
	
	private int tenantId=1;
	private int operatorid=1;
	private int promotionId =1 ;
	private String userName="abc";
	private String userName2="edf";
	private long userId =1;
	private long bonusId = 1;
	
	
	private WebClient client, client2;
	
	//private final String server = "http://52.1.96.115:8080/vol-appserver/";
	private final String server = "http://localhost:8080/vol-admin/rs/";

	private void initClient(){
		JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
		FormEncodingProvider formProvider = new FormEncodingProvider(true);
		List providers = new ArrayList();
		providers.add(jsonProvider);
		providers.add(formProvider);
		
		client = WebClient.create(server+"admin",providers);
		WebClient.getConfig(client).getInInterceptors().add(new LoggingInInterceptor());
		WebClient.getConfig(client).getOutInterceptors().add(new LoggingOutInterceptor()); 
		
		// public service 
		client2 = WebClient.create(server+"public",providers);
		WebClient.getConfig(client2).getInInterceptors().add(new LoggingInInterceptor());
		WebClient.getConfig(client2).getOutInterceptors().add(new LoggingOutInterceptor());
	}
	@Test
	public void test(){
		initClient();
		
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
		promotion.setMaximum(270001);
		promotion.setRule("if(parameters.vip==1) {return 2300;} else {return 200;}");
		

		
		if(getTenantByName(tanent.getName()) == null){
			createTenant( tanent);
		}
		
		showTenant(tenantId);
		
		if(getOperatorByName(operator.getName()) == null){
			createOperator(operator);
		}
		showOperator(operatorid);
		if(getPromotionByName(promotion.getName()) == null){
			createPromotion(promotion);
		}
		
		showPromotion(promotionId);	
		activePromotion(promotionId);
		listPublicPromotion(client2);	
		crabBonus(promotionId, userName);	
		
		showUser(userName);
		//showUserBonus(userId);

		showUserBonus(userName);
		checkPromotionbalance(promotionId);			
		
		activeBonus(bonusId);	
		
		checkActivatedBonus(userId,bonusId);
		
		checkQuota(userId);
		checkQuota(userName);
	}
	
	@Test
	public void testTransfer(){
		initClient();
		
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
		promotion.setMaximum(270000);
		promotion.setRule("if(parameters.vip==1) {return 2300;} else {return 200;}");
		

		
		if(getTenantByName(tanent.getName()) == null){
			createTenant( tanent);
		}
		
		showTenant(tenantId);
		
		if(getOperatorByName(operator.getName()) == null){
			createOperator(operator);
		}
		showOperator(operatorid);
		if(getPromotionByName(promotion.getName()) == null){
			createPromotion(promotion);
		}
		
		showPromotion(promotionId);	
		activePromotion(promotionId);
		listPublicPromotion(client2);	
		crabBonus(promotionId, userName);	
		crabBonus(promotionId, userName2);	
		
		showUser(userName);
		//showUserBonus(userId);

		showUserBonus(userName);
		checkPromotionbalance(promotionId);			
		
		transferBonus(bonusId,userName,userName2);
		//showUserBonus(userName);
		showUserBonus(userName2);
	}
	/**
	 * @param userId 
	 * 
	 */
	private void checkQuota(long userId) {
		Response restResult;
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
	
	/**
	 * @param userId 
	 * 
	 */
	private void checkQuota(String userName) {
		Response restResult;
		client2.reset();
		//check quota
		System.out.println("check quota by userName");
		client2.path("quota/"+tenantId);
		client2.query("userName", userName);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Quota>> quotalistType = new GenericType<List<Quota>>(){};
		List<Quota> quota = restResult.readEntity(quotalistType);
		System.out.println("get quotas:"+quota);
	}
	
	/**
	 * @param bonusId 
	 * 
	 */
	private void checkActivatedBonus(long userId, long bonusId) {
		Response restResult;
		client2.reset();
		System.out.println("check activated bonus");
		//check bonus
		client2.path("bonus/"+tenantId+"/"+userId+"/"+bonusId);
		client2.type(form).accept(json);
		restResult = client2.get();
		Bonus bonus = restResult.readEntity(Bonus.class);
		System.out.println("get bonus:"+bonus);
	}
	
	private void transferBonus(long bonusId, String fromUser, String toUser) {
		Response restResult;
		client2.reset();
		
		System.out.println("transfer bonus");
		client2.path("sendbonus/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.put("bonusId="+bonusId+"&toUser="+toUser+"&fromUser="+fromUser);
		boolean b = restResult.readEntity(Boolean.class);
		System.out.println("transfer bonues :"+b);
	}
	
	/**
	 * @param bonusId 
	 * 
	 */
	private void activeBonus(long bonusId) {
		Response restResult;
		client2.reset();
		
		System.out.println("active bonus");
		client2.path("activebonus/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.post("bonusId="+bonusId);
		boolean b = restResult.readEntity(Boolean.class);
		System.out.println("get active bonues :"+b);
	}
	/**
	 * @param promotionId 
	 * 
	 */
	private void checkPromotionbalance(int promotionId) {
		Response restResult;
		//check promotion balance
		client.reset();

		System.out.println("check promotionbalance");
		client.path("promotionbalance");
		client.type(json).accept(json);
		client.query("promotionId", promotionId);
		restResult = client.get();
		PromotionBalance balance = restResult.readEntity(PromotionBalance.class);
		System.out.println("get promotion balance:"+balance);
	}
	/**
	 * @param userId
	 * @return
	 */
/*	private long showUserBonus(long userId) {
		Response restResult;
		client2.reset();
		
	
		System.out.println("check user's bonus");
		//check bonus
		client2.path("bonus/"+tenantId+"/"+userId);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Bonus>> bonusListType = new GenericType<List<Bonus>>(){};
		List<Bonus> bonuses = restResult.readEntity(bonusListType);
		System.out.println("list bonuses:"+bonuses);
		 bonusId = bonuses.get(bonuses.size()-1).getId();
		return bonusId;
	}*/
	
	/**
	 * @param userName
	 * @return
	 */
	private long showUserBonus(String userName) {
		Response restResult;
		client2.reset();
		
	
		System.out.println("check user's bonus by name");
		//check bonus
		client2.path("bonus/"+tenantId);
		client2.query("userName", userName);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Bonus>> bonusListType = new GenericType<List<Bonus>>(){};
		List<Bonus> bonuses = restResult.readEntity(bonusListType);
		System.out.println("list bonuses:"+bonuses);
		 bonusId = bonuses.get(bonuses.size()-1).getId();
		return bonusId;
	}
	
	
	/**
	 * @param userName 
	 * @return
	 */
	private long showUser(String userName) {
		Response restResult;
		client2.reset();
		//get user info
		System.out.println("get User info");
		client2.path("user/"+tenantId);
		client2.type(json).accept(json);
		client2.query("userName", userName);
		restResult = client2.get();
		User usr = restResult.readEntity(User.class);
		System.out.println("get user info :"+usr);		
		userId = usr.getId();
		return userId;
	}
	/**
	 * @param promotionId 
	 * 
	 */
	private void crabBonus(int promotionId,String userName) {
		Response restResult;
		client2.reset();
		
		//grab
		System.out.println("Grab .....");
		client2.path("getbonus/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.post("promotionId="+promotionId+"&userName="+userName+"&vip=1");
		BunosResult br = restResult.readEntity(BunosResult.class);
		System.out.println("get bonusResult :"+br);
	}

	/**
	 * @param client2
	 */
	private void listPublicPromotion(WebClient client2) {
		Response restResult;
		client2.reset();
		System.out.println("list  promotion from public");
		client2.path("promotion/"+tenantId);
		client2.type(form).accept(json);
		restResult = client2.get();
		 GenericType<List<Promotion>> promotionListType = new GenericType<List<Promotion>>(){};
		List<Promotion> promotions = restResult.readEntity(promotionListType);
		System.out.println("list promotion:"+promotions);
	}

	/**
	 * @param promotionId 
	 */
	private void activePromotion(int promotionId) {
		Response restResult;
		client.reset();
		
		System.out.println("active  promotion");
		//active promotion
		client.path("promotion/active/"+promotionId);
		client.type(json).accept(json);
		restResult = client.post(null);
		OperationResult result4 = restResult.readEntity(OperationResult.class);
		System.out.println("active promotion:"+result4);
	}

	/**
	 * @param promotionId 
	 */
	private void showPromotion(int promotionId) {
		Response restResult;
		client.reset();
		
		System.out.println("get  promotion");

		client.path("promotion/"+tenantId+"/"+promotionId);
		client.type(json).accept(json);
		restResult = client.get();
		Promotion promotion1 = restResult.readEntity(Promotion.class);
		System.out.println("get promotion:"+promotion1);
	}
	
	
	/**
	 * @param promotionId 
	 * @return 
	 */
	private Promotion getPromotionByName(String name) {
		Response restResult;
		client.reset();
		
		System.out.println("get  promotion");

		client.path("promotion/"+tenantId);
		client.query("query", "byName");
		client.query("name", name);
		client.type(json).accept(json);
		restResult = client.get();
		List<Promotion> promotion1;
		 GenericType<List<Promotion>> promotionListType = new GenericType<List<Promotion>>(){};
		promotion1 = restResult.readEntity(promotionListType);
		System.out.println("get promotion:"+promotion1);
		if(promotion1.isEmpty()){
			return null;
		}
		this.promotionId = promotion1.get(0).getId();
		return promotion1.get(0);
	}


	/**
	 * @param promotion
	 * @return
	 */
	private int createPromotion(Promotion promotion) {
		Response restResult;
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
		promotionId = (int) result3.getId();
		return promotionId;
	}

	/**
	 * @param operatorid 
	 */
	private void showOperator(int operatorid) {
		Response restResult;
		client.reset();
		System.out.println("Check Operator");
		client.path("operator/"+operatorid);
		client.type(json).accept(json);
		restResult = client.get();
		Operator operator1 = restResult.readEntity(Operator.class);
		System.out.println("get operator:"+operator1);
	}
	
	
	private Operator getOperatorByName(String name) {
		Response restResult;
		client.reset();
		System.out.println("Check Operator");
		client.path("operator");
		client.query("query", "byName");
		client.query("name", name);
		client.type(json).accept(json);
		restResult = client.get();
		List<Operator> operator1;
		 GenericType<List<Operator>> operatorListType = new GenericType<List<Operator>>(){};
		operator1 = restResult.readEntity(operatorListType);
		System.out.println("get operator:"+operator1);
		if(operator1.isEmpty()){
			return null;
		}
		this.operatorid = operator1.get(0).getId();
		return operator1.get(0);
	}

	/**
	 * @param operator
	 */
	private void createOperator(Operator operator) {
		Response restResult;
		client.reset();
		
		// create operator
		System.out.println("creating Operator");
		client.path("operator");
		client.type(json).accept(json);
		
		operator.setTenantId(tenantId);
		restResult = client.put(operator);
		PutOperationResult result2 = restResult.readEntity(PutOperationResult.class);
		System.out.println("put operator "+result2);
		operatorid = (int) result2.getId();
	}

	/**
	 * @param tanent
	 * @return
	 */
	private int createTenant(Tenant tanent) {
		Response restResult;
		client.reset();
		// create tenant
		System.out.println("creating Tenant");
		client.path("tenant");
		client.type(json).accept(json);
		
		restResult = client.put(tanent);
		PutOperationResult result = restResult.readEntity(PutOperationResult.class);
		System.out.println("put tenant "+result);

		tenantId = (int) result.getId();
		return tenantId;
	}

	/**
	 * @param tenantId
	 */
	private void showTenant(int tenantId) {
		Response restResult;
		client.reset();
		System.out.println("check Tenant");

		client.path("tenant/"+tenantId);
		restResult = client.get();
		Tenant tenant1 = restResult.readEntity(Tenant.class);
		System.out.println("get tenant:"+tenant1);
	}
	
	private Tenant getTenantByName(String name) {
		Response restResult;
		client.reset();
		System.out.println("get Tenant by name");

		client.path("tenant");
		client.query("query", "byName");
		client.query("name", name);
		restResult = client.get();
		List<Tenant> tenant1;
		 GenericType<List<Tenant>> tenantListType = new GenericType<List<Tenant>>(){};
		tenant1 = restResult.readEntity(tenantListType);
		System.out.println("getbyName tenant:"+tenant1);
		if(tenant1.isEmpty()){
			return null;
		}
		this.tenantId = tenant1.get(0).getId();
		return tenant1.get(0);
	}
}
