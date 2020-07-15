package com.test.platform.apisetting;


import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@SuppressWarnings("hiding")
public class ApiRes<T> extends ResponseEntity<T>{
	
	public ApiRes() {
		super(null, null, HttpStatus.OK);
	}

	public ApiRes(@Nullable T body) {
		super(body, null, HttpStatus.OK);
	}
	
	public static Map<String, Object> error(Integer code, Object msg) {
		Map<String, Object> response = new HashMap<>();
		response.put("code", code);
        response.put("msg", msg);
		return response;
	}
	
	public static Map<String, Object> error(Integer code, Object msg, Map<String, Object> data) {
		Map<String, Object> response = new HashMap<>();
		response.put("code", code);
        response.put("msg", msg);
        response.putAll(data);
		return response;
	}

}
