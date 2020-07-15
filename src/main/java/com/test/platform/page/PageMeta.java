package com.test.platform.page;

import java.io.Serializable;

import lombok.Data;

@Data
public class PageMeta implements Serializable {
	private static final long serialVersionUID = 1L;

	public PageMeta() {}
	
	public PageMeta(Long total) {
		this.total = total;
	}
	private Long total;
	
	private Long current_page;
}
