package com.test.business.domain.module1.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.business.domain.module1.model.StudentModel;
import com.test.business.domain.module1.repository.StudentRepository;
import com.test.business.domain.module1.service.StudentDomainService;
import com.test.business.interfaces.formanage.dto.StudentDto;
import com.test.business.interfaces.formanage.dto.StudentForAddDto;
import com.test.business.interfaces.formanage.dto.StudentForUpdateDto;
import com.test.platform.exception.BusinessException;
import com.test.platform.page.ResPage;

@Service
public class StudentDomainServiceImpl implements StudentDomainService {
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public ResPage<StudentDto> getStudents(int pageSize, int page) {
		ResPage<StudentDto> res = new ResPage<StudentDto>();
		IPage<StudentModel> pageQuery = new Page<>();
		pageQuery.setCurrent(page);
		pageQuery.setSize(pageSize);
		IPage<StudentModel> result = studentRepository.page(pageQuery);
		if(result != null) {
			if(result.getRecords() != null && result.getRecords().size() > 0) {
				result.getRecords().forEach(item -> {
					StudentDto dto = new StudentDto();
					BeanUtils.copyProperties(item, dto);
					res.getData().add(dto);
				});
			}
			res.setCurrentPage(page);
			res.setTotal(result.getTotal());
		}
		return res;
	}

	@Override
	public StudentDto getStudentById(String studentId) {
		StudentDto result = null;
		StudentModel model = studentRepository.getById(studentId);
		if(model != null) {
			result = new StudentDto();
			BeanUtils.copyProperties(model, result);
		}
		return result;
	}

	@Override
	public StudentDto postStudent(StudentForAddDto studentForAddDto) {
		StudentDto res = null;
		if(studentForAddDto != null) {
			StudentModel model = new StudentModel();
			BeanUtils.copyProperties(studentForAddDto, model);
			model = studentRepository.saveAndReturn(model);
			res = new StudentDto();
			BeanUtils.copyProperties(model, res);
		} else {
			throw new BusinessException("添加对象为空");
		}
		return res;
	}

	@Override
	public void deleteStudent(String studentId) {
		StudentModel model = studentRepository.getById(studentId);
		if(model == null) {
			throw new BusinessException("需要删除的学生不存在");
		}
		studentRepository.removeById(studentId);
	}

	@Override
	public StudentDto putStudent(String studentId, StudentForUpdateDto studentForUpdateDto) {
		StudentDto res = null;
		if(studentForUpdateDto != null) {
			if(studentId == null || "".equals(studentId)) {
				throw new BusinessException("projectId为空");
			}
			StudentModel model = studentRepository.getById(studentId);
			if(model == null) {
				throw new BusinessException("对象不存在");
			}
			BeanUtils.copyProperties(studentForUpdateDto, model);
			model = studentRepository.updateAndReturn(model);
			res = new StudentDto();
			BeanUtils.copyProperties(model, res);
		} else {
			throw new BusinessException("更新对象为空");
		}
		return res;
	}

}
