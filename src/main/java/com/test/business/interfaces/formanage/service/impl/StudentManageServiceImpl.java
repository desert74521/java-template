package com.test.business.interfaces.formanage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.business.domain.module1.service.StudentDomainService;
import com.test.business.interfaces.formanage.dto.StudentDto;
import com.test.business.interfaces.formanage.dto.StudentForAddDto;
import com.test.business.interfaces.formanage.dto.StudentForUpdateDto;
import com.test.business.interfaces.formanage.service.StudentManageService;
import com.test.platform.page.ResPage;

@Service
public class StudentManageServiceImpl implements StudentManageService {
	
	@Autowired
	private StudentDomainService studentDomainService;

	@Override
	public ResPage<StudentDto> getStudents(int pageSize, int page) {
		return studentDomainService.getStudents(pageSize, page);
	}

	@Override
	public StudentDto getStudentById(String studentId) {
		return studentDomainService.getStudentById(studentId);
	}

	@Override
	public StudentDto postStudent(StudentForAddDto studentForAddDto) {
		return studentDomainService.postStudent(studentForAddDto);
	}

	@Override
	public void deleteStudent(String studentId) {
		studentDomainService.deleteStudent(studentId);
		
	}

	@Override
	public StudentDto putStudent(String studentId, StudentForUpdateDto studentForUpdateDto) {
		return studentDomainService.putStudent(studentId, studentForUpdateDto);
	}

}
