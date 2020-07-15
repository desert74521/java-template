package com.test.platform.orm;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class BaseModel {
	
	@TableId(type= IdType.AUTO)
	private String id;
	
	/**创建时间*/
	private Date createAt;
	
	/**更新时间*/
	private Date updateAt;
	
	/**删除标志*/
	private String delFlag;
	
}
