package com.test.business.interfaces.formanage.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.business.interfaces.formanage.dto.StudentDto;
import com.test.business.interfaces.formanage.dto.StudentForAddDto;
import com.test.business.interfaces.formanage.dto.StudentForUpdateDto;
import com.test.business.interfaces.formanage.service.StudentManageService;
import com.test.platform.aop.annotation.Auth;
import com.test.platform.apisetting.ApiRes;
import com.test.platform.page.ResPage;

@RestController
@RequestMapping("/manage/student")
public class StudentManageApi {
	
	@Autowired
	private StudentManageService studentManageService;
	
	/**
	 * @apiNote 分页获取列表
	 */
	@Auth
	@GetMapping()
	public ApiRes<ResPage<StudentDto>> getStudents(
			@RequestParam("pageSize") int pageSize, 
			@RequestParam("page") int page) {
		ResPage<StudentDto> resultPage = studentManageService.getStudents(pageSize, page);
		return new ApiRes<ResPage<StudentDto>>(resultPage);
	}
	
	/**
	 * @apiNote 按id获取单个
	 */
	@Auth
	@GetMapping("/{id}")
	public ApiRes<StudentDto> getStudentById(
			@PathVariable("id") String studentId) {
		StudentDto result = studentManageService.getStudentById(studentId);
		return new ApiRes<StudentDto>(result);
	}
	
	/**
	 * @apiNote 插入一个新的
	 */
	@Auth
	@PostMapping()
	public ApiRes<StudentDto> postStudent(
			@Valid @RequestBody StudentForAddDto studentForAddDto) {
		StudentDto result = studentManageService.postStudent(studentForAddDto);
		return new ApiRes<StudentDto>(result);
	}
	
	/**
	 * @apiNote 删除一个
	 */
	@Auth
	@DeleteMapping("/{id}")
	public ApiRes<String> deleteStudent(
			@PathVariable("id") String studentId) {
		studentManageService.deleteStudent(studentId);
		return new ApiRes<String>("success");
	}
	
	/**
	 * @apiNote 更新一个
	 */
	@Auth
	@PutMapping("/{id}")
	public ApiRes<StudentDto> putStudent(
			@PathVariable("id") String studentId, 
			@Valid @RequestBody StudentForUpdateDto studentForUpdateDto) {
		StudentDto result = studentManageService.putStudent(studentId, studentForUpdateDto);
		return new ApiRes<StudentDto>(result);
	}
}
