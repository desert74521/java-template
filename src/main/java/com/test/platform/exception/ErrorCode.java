package com.test.platform.exception;

public class ErrorCode {

	public static final Integer SERNALNUMBER_FORMAT_ERROR=4000;

	public static final Integer SERNALNUMBER_EXCEL_TITLE_ERROR=4001;

	public static final Integer SERNALNUMBER_EXCEL_DATA_ERROR=4002;

	public static final Integer SERNALNUMBER_EXCEL_BROWSER_ERROR=4003;

	public static final Integer SERNALNUMBER_REPEAT_ERROR=4004;

	public static final Integer EXIT_ASSOCIATED_DEVICE =4006;

	public static final Integer PLAN_IS_NOT_EXIST=4007;

	public static final Integer SERNAL_IS_NOT_EXIST=4008;

	public static final Integer NO_PERMISSION_PERFORM_RECALL=4009;

	public static final Integer ONLY_DELETE_SUBMITED=4010;

	public static final Integer APPLICATION_NOT_EXIST=4011;

	public static final Integer SERIAL_MAC_IS_NOT_CONSISTENT=4012;

	public static final Integer MAC_ERROR=4013;

	public static final Integer CUSTOMER_NOT_EXIT=4014;

	public static final Integer EXPORT_EXCEEDS_MAXIMUM_LIMIT=4015;

	public static final Integer JSON_ERROR = 9999;

	public static final Integer SYSTEM_ERROR = 10000;
	
	/**accessToken error*/
	public static final Integer OTA_TOKEN_ERROR = 400100;
	
	/**accessToken解析失败*/
	public static final Integer OTA_TOKEN_PARSE_ERROR = 400101;
	
	/**param[status] must equals 'successed' or 'failed'*/
	public static final Integer OTA_RECORD_PARAMS_ERROR = 400102;
	
	/**softwareApiId error*/
	public static final Integer OTA_SOFTWARE_ID_ERROR = 400103;
	
	/**version error*/
	public static final Integer OTA_SOFTWARE_VERSION_ERROR = 400104;
	
	/**appId or appSecret error*/
	public static final Integer OTA_APPID_OR_SECRET_ERROR = 400105;
	
}
