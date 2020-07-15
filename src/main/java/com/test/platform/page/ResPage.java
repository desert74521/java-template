package com.test.platform.page;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class ResPage<T> {

	@ApiModelProperty(value="分页具体数据")
	private List<T> data = new ArrayList<>();
	
	@ApiModelProperty(value="分页概要信息")
	private PageMeta meta = new PageMeta();
	
	public void setRecords(List<T> data) {
		this.data = data;
	}
	
	public void setCurrentPage(long size) {
		this.meta.setCurrent_page(size);
	}
	
	public void setTotal(long total) {
		this.meta.setTotal(total);
	}
}
