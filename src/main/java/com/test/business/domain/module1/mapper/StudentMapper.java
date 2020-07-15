package com.test.business.domain.module1.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.business.domain.module1.model.StudentModel;

@DS("test")
@Mapper
public interface StudentMapper extends BaseMapper<StudentModel>{

}
