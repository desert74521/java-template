package com.test.protocol.oss.vo;

import lombok.Data;

@Data
public class OssTokenVo {
	private String accessid;
	
	private String policy;
	
	private String signature;
	
	private String dir;
	
	private String host;
}
