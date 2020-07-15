package com.test.platform.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class PageAdapter implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Map<String,Object>> data;
	
	private Map<String,Object> links;
	
	private Map<String,Object> meta;
}
