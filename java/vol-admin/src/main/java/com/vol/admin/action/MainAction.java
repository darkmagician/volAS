/**
 * 
 */
package com.vol.admin.action;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.vol.common.tenant.Operator;
import com.vol.common.tenant.Tenant;
import com.vol.mgmt.OperatorMgmtImpl;
import com.vol.mgmt.TenantMgmtImpl;
import com.vol.mgmt.auth.Authentication;
import com.vol.promotion.rule.PromotionPolicyService;

/**
 * @author scott
 *
 */
public class MainAction extends ActionSupport {
	private static final Logger log = LoggerFactory.getLogger(MainAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String promotionSourceVals;
	static{
		ObjectMapper m = new ObjectMapper();
		String json;
		try {
			String [] list = PromotionPolicyService.getSourceList();
			Map<String,String> [] maps = new Map[list.length];
			for(int i=0;i<list.length;i++){
				maps[i]=Collections.singletonMap("value", list[i]);
			}
			json=m.writeValueAsString(maps);
		} catch (Exception e) {
			json="";
		}
		promotionSourceVals=json;
	}
	
	
	private Operator operator;
	
	private String tenantName;
	

	
	@Resource(name="tenantMgmt")
	private TenantMgmtImpl tenantMgmt;
	
	@Resource(name="operatorMgmt")
	private OperatorMgmtImpl operatorMgmt;
	

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
		Operator operator = (Operator)session.getAttribute("operator");
		if(operator == null){
			
			Principal principal = request.getUserPrincipal();
			String name = principal.getName();
			operator = Authentication.locateOperator(operatorMgmt, name);
			if(operator == null){
				log.error("Operator is not identified by name {}",name);
				return Action.ERROR;
			}
			int tenantId = operator.getTenantId();
			if(tenantId!=0){
				Tenant tenant = tenantMgmt.get(tenantId);
				if(tenant == null){
					log.error("Tenant is not identified by id: {}",tenantId);
					return Action.ERROR;
				}
				session.setAttribute("tenantName", tenant.getName());
			}
			session.setAttribute("operator", operator);
			log.info("Operator {} login successfully!",operator.getName());
		}
		this.tenantName = (String)session.getAttribute("tenantName");
		this.operator = operator;
		return Action.SUCCESS;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}

	/**
	 * @param tenantName the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	/**
	 * @return the promotionsourcevals
	 */
	public static String getPromotionsourcevals() {
		return promotionSourceVals;
	}

	
}
