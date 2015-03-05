/**
 * 
 */
package com.vol.common.exception;

/**
 * @author Dai Zhi Jie
 *
 */
public enum ErrorCode {
	SUCCESS(2001),
	UNLUCY(2002),
	
	BUSY(3001),
	INVALID_PROMOTION(3002,"无效活动"),
	PROMOTION_USEDUP(3002,"活动流量已经抢光"),
	
	
	INTERNAL_ERROR(5001,"内部错误"),
	ENTITY_NOT_EXIST(5002,"操作对象不存在"),
	OPERATION_NOT_SUPPORT(5003,"操作不支持"),
	CONCURRENT_MODIFICATION(5004,"有人同时修改了数据，请重新刷新后修改"),
	DELETE_NOT_ALLOW(5005,"不允许删除"),
	MODIFICATION_NOT_ALLOW(5006,"不允许修改"),
	
	EMPTY_FIELD(5010,"字段不能为空"),
	NULL_OBJECT(5011,"操作对象没有提供"),
	EMPTY_DATE_FILED(5012,"请设置时间"),
	START_LATER_THAN_END(5013,"开始时间不能晚于结束时间"),
	END_LATER_THAN_EXPIRATION(5014,"红包过期时间不能早于活动结束时间"),
	INVALID_PASSWORD(5015,"密码不正确"),
	OPERATOR_NOT_FOUND(5016,"无法识别当前管理员，可能已经删除。"),
	OPERATOR_RULE_INVALID(5017,"活动规则错误"),
	
	UNKNOWN_OPERATION(6001,"未知操作"),
	OPERATOR_NOT_IDENTIFIED(6002,"无法识别当前管理员，请重新登录"),
	PERMISSION_DENIED(6003,"权限不允许"),
	PRIMARY_KEY_MISSING(6004,"对象主键未提供"),
	;
	
    private final int code;
    private final String description;
    
    
    private ErrorCode( int code) {
        this.code = code;
        description=null;
    }
    
    private ErrorCode( int code, String description) {
        this.description = description;
        this.code = code;
    }
    
	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
}
