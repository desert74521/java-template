package com.test.business.domain.module1.service;

import com.test.business.interfaces.formanage.dto.StudentDto;
import com.test.business.interfaces.formanage.dto.StudentForAddDto;
import com.test.business.interfaces.formanage.dto.StudentForUpdateDto;
import com.test.platform.page.ResPage;

public interface StudentDomainService {
	
	public ResPage<StudentDto> getStudents(int pageSize, int page);
	
	public StudentDto getStudentById(String studentId);
	
	public StudentDto postStudent(StudentForAddDto studentForAddDto);
	
	public void deleteStudent(String studentId);
	
	public StudentDto putStudent(String studentId, StudentForUpdateDto studentForUpdateDto);
}
