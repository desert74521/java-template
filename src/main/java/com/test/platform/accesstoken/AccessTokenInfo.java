package com.test.platform.accesstoken;

import java.util.Map;

import lombok.Data;

@Data
public class AccessTokenInfo {
	
	private String id;
	
	private Map<String, Object> claims;
}
