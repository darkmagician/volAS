package com.vol.rest.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.tenant.Operator;

public class RestContext {
	
	protected final static String PRODUCER_TYPE_FOR_SUBMIT = MediaType.TEXT_PLAIN;

	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Context
	protected HttpServletRequest httppServletRequest;
	@Resource(name = "restSecurity")
	protected Boolean securityCheck = true;

	public RestContext() {
		super();
	}

	protected void checkPermission(Integer tenantId) {
		if(!securityCheck){
			return;
		}
		Operator operator = getCurrentOperator();
		if(operator.getTenantId() == 0 || (tenantId!=null && tenantId == operator.getTenantId()) ){
			return;
		}
		throw new MgmtException(ErrorCode.PERMISSION_DENIED);
	}

	protected Operator getCurrentOperator() {
		if(!securityCheck){
			return new Operator();
		}
		Operator operator = null;
		HttpSession session = httppServletRequest.getSession();
		if(session != null){
			operator = (Operator) session.getAttribute("operator");
		}
		if(operator == null){
			throw new MgmtException(ErrorCode.OPERATOR_NOT_IDENTIFIED);
		}
		return operator;
	}

}