package com.test.platform.exception;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private Integer code;
	
	public BusinessException(Integer code, String errorMsg) {
		super(errorMsg);
		this.code = code;
	}
	
	public BusinessException(String errorMsg) {
		super(errorMsg);
		this.code = ErrorCode.SYSTEM_ERROR;
	}
	
	public Integer getErrorCode() {
		return this.code;
	}
	
}
