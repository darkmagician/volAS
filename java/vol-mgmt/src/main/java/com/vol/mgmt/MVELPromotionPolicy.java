/**
 * 
 */
package com.vol.mgmt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.vol.common.exception.ErrorCode;
import com.vol.common.exception.MgmtException;
import com.vol.common.service.PromotionPolicy;
import com.vol.common.tenant.Promotion;
import com.vol.common.user.Bonus;
import com.vol.common.user.User;

/**
 * @author scott
 *
 */
public class MVELPromotionPolicy implements PromotionPolicy {

	private final Map<String, Class> imports = initContext();
	
	/* (non-Javadoc)
	 * @see com.vol.common.service.PromotionPolicy#evaluate(com.vol.common.tenant.Promotion, java.util.Map)
	 */
	@Override
	public Long evaluate(Promotion promotion, Map<String, Object> context) {
		Serializable compiled = promotion.getCompiled();
		if(compiled != null){
			return MVEL.executeExpression(compiled, context,Long.class);
		}else{
			String rule = promotion.getRule();
			return MVEL.eval(rule, context, Long.class);
		}
	}
	
	
	@Override
	public void precompile(Promotion promotion){
		String rule = promotion.getRule();
		ParserContext ctx = new ParserContext() ;
		ctx.setInputs(imports);
		ctx.setStrongTyping(true);
		Serializable compiled = MVEL.compileExpression(rule, ctx);
		promotion.setCompiled(compiled);
	}
	
	@Override
	public void validate(Promotion promotion){
		String rule = promotion.getRule();
		
		
		try {
			ParserContext ctx = new ParserContext() ;
			ctx.setInputs(imports);
			ctx.setStrongTyping(true);
			MVEL.analysisCompile(rule, ctx );
		} catch (Exception e) {
			throw new MgmtException(ErrorCode.OPERATOR_RULE_INVALID,"Validate Rule Failed",e, e.getLocalizedMessage());
		}
	}


	private Map<String, Class> initContext() {
		Map<String, Class> imports = new HashMap<String, Class>();
		imports.put(PromotionPolicy.PARAMETERS, Map.class);
		imports.put(PromotionPolicy.BALANCE, Long.class);
		imports.put(PromotionPolicy.GRANTED, Bonus[].class);
		imports.put(PromotionPolicy.PROMOTION, Promotion.class);
		imports.put(PromotionPolicy.USER, User.class);
		return imports;
	}


}
