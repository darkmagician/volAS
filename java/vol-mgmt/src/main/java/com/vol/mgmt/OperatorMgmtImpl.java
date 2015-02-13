/**
 * 
 */
package com.vol.mgmt;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.vol.common.DAO;
import com.vol.common.tenant.Operator;
import com.vol.dao.AbstractService;

/**
 * @author scott
 *
 */
public class OperatorMgmtImpl extends AbstractService{

	@Resource(name="operatorDao")
	protected DAO<Integer,Operator> operatorDAO;
	
	public Integer addOperator(final Operator operator){
		validateOperator(operator);
		Integer id = this.transaction.execute(new TransactionCallback<Integer>(){

			@Override
			public Integer doInTransaction(TransactionStatus status) {
				initEntity(operator);
				return operatorDAO.create(operator);
			}

			});
		if(log.isDebugEnabled()){
			log.debug("promotion id={} is added", id);
		}		
		return id;
	}
	
	private void validateOperator(Operator operator) {
		//if(promo)
		
	}
	
	public void updateOperator(final Operator operator){
		validateOperator(operator);
		this.transaction.execute(new TransactionCallbackWithoutResult(){

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				Operator old = operatorDAO.get(operator.getId());
				validateUpdateTime(operator, old);
				updateEntity(operator);
				operatorDAO.update(operator);
				
			}


		});
	}
	
	public Operator getOperator(final Integer id){
		Operator operator = this.readonlyTransaction.execute(new TransactionCallback<Operator>(){

			@Override
			public Operator doInTransaction(TransactionStatus status) {
				return operatorDAO.get(id);
			}});	
		return operator;
	}
	
	public List<Operator> list(final String queryName, final Map<String,Object> parameters){
		return this.readonlyTransaction.execute(new TransactionCallback<List<Operator>>(){

			@Override
			public List<Operator> doInTransaction(TransactionStatus status) {
				return operatorDAO.query(queryName, parameters);
			}});	
	}
}
