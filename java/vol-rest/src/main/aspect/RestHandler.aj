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
	

    pointcut listOperation():
        execution(public List * (..));     
        
    pointcut getOperation():
        execution(public * get (..));           
        
         

 
 	OperationResult around(Tracable t): this(t) && (execution(public OperationResult * (..))){
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
			result.setErrorCode(me.getCode(),me.getAdditional());
			return result;
		} catch (Exception e){
			logger.error("Operation Unexpected Error - "+thisJoinPoint.getSignature().getName(),e);
			OperationResult result = new OperationResult();
			result.setErrorCode(ErrorCode.INTERNAL_ERROR);
			return result;
		}
    }  


 	PutOperationResult around(Tracable t): this(t) && (execution(public PutOperationResult * (..))){
    	Logger logger =  t.logger;

		try{
		    if(logger.isTraceEnabled()){
        		logger.trace("Entering Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
			PutOperationResult result = proceed(t);
			if(logger.isTraceEnabled()){
        		logger.trace("Returning Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
       		return result;
		}catch (MgmtException me) {
			logger.error("Operation Error - "+thisJoinPoint.getSignature().getName(),me);
			PutOperationResult result = new PutOperationResult();
			result.setErrorCode(me.getCode(),me.getAdditional());
			return result;
		} catch (Exception e){
			logger.error("Operation Unexpected Error - "+thisJoinPoint.getSignature().getName(),e);
			PutOperationResult result = new PutOperationResult();
			result.setErrorCode(ErrorCode.INTERNAL_ERROR);
			return result;
		}
    }  
  	PagingOperationResult around(Tracable t): this(t) && ( execution(public PagingOperationResult * (..))){
    	Logger logger =  t.logger;

		try{
		    if(logger.isTraceEnabled()){
        		logger.trace("Entering Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
			PagingOperationResult result = proceed(t);
			if(logger.isTraceEnabled()){
        		logger.trace("Returning Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
       		return result;
		}catch (MgmtException me) {
			logger.error("Operation Error - "+thisJoinPoint.getSignature().getName(),me);
			PagingOperationResult result = new PagingOperationResult();
			result.setErrorCode(me.getCode(),me.getAdditional());
			return result;
		} catch (Exception e){
			logger.error("Operation Unexpected Error - "+thisJoinPoint.getSignature().getName(),e);
			PagingOperationResult result = new PagingOperationResult();
			result.setErrorCode(ErrorCode.INTERNAL_ERROR);
			return result;
		}
    } 
    
    
    
     Object around(Tracable t): this(t) && (listOperation() || getOperation()){
    	Logger logger =  t.logger;

		try{
		    if(logger.isTraceEnabled()){
        		logger.trace("Entering Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
			Object result = proceed(t);
			if(logger.isTraceEnabled()){
        		logger.trace("Returning Operation: {}" , thisJoinPoint.getSignature().getName());
       		}
       		return result;
		} catch (Exception e){
			logger.error("Operation Error - "+thisJoinPoint.getSignature().getName(),e);
			
			return null;
		}
    }        
}