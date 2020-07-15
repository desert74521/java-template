package com.test.business.domain.module1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.test.platform.orm.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("test_student")
public class StudentModel extends BaseModel{

	private String name;
	
	private Integer age;
}
