package com.vol.rest.aop;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.vol.rest.result.*;
import com.vol.common.exception.*;

public aspect RestHandler {

	declare parents: ( com.vol.rest.service.* ) implements Tracable;
	 
	 
	protected interface Tracable {
		public Logger initialLogger();
	}
	
	private final Logger Tracable.logger = initialLogger();
	
	public Logger Tracable.initialLogger(){
		return LoggerFactory.getLogger(getClass());
	}
	

	pointcut operation():
		execution(public OperationResult * (..));
    
    pointcut putOperation():
        execution(public PutOperationResult * (..));
        
    pointcut pagingOperation():
        execution(public PagingOperationResult * (..));            
	
    pointcut listOperation():
        execution(public List * (..));     
        
    pointcut getOperation():
        execution(public * get (..));           
        
         

 
 	OperationResult around(Tracable t): this(t) && (operation()){
    	Logger logger =  t.logger;

		try{
		    if(logger.isTraceEnabled()){
        		logger.trace("Entering Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
			OperationResult result = proceed(t);
			if(logger.isTraceEnabled()){
        		logger.trace("Returning Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
       		return result;
		}catch (MgmtException me) {
			logger.error("Operation Error - "+thisJoinPoint.getSignature().getName(),me);
			OperationResult result = new OperationResult();
			result.setErrorCode(me.getCode());
			return result;
		} catch (Exception e){
			logger.error("Operation Unexpected Error - "+thisJoinPoint.getSignature().getName(),e);
			OperationResult result = new OperationResult();
			result.setErrorCode(ErrorCode.INTERNAL_ERROR);
			return result;
		}

    }  

}