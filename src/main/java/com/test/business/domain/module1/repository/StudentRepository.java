package com.test.business.domain.module1.repository;

import org.springframework.stereotype.Service;

import com.test.business.domain.module1.mapper.StudentMapper;
import com.test.business.domain.module1.model.StudentModel;
import com.test.platform.orm.ServiceImplLocal;

@Service
public class StudentRepository  extends ServiceImplLocal<StudentMapper, StudentModel>{

}
