package com.test.platform.orm;

import java.io.Serializable;

import com.baomidou.mybatisplus.extension.service.IService;
/**
 * @author sxj
 * @apiNote 拓展IService接口实现软删除,及更新字段赋值
 */
public interface IServiceLocal<T extends BaseModel> extends IService<T>{
	
	public T getByIdContainDel(Serializable id);
	
	public T saveAndReturn(T model);
	
	public T updateAndReturn(T model);
	
}
