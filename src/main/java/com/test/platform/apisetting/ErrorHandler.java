package com.test.platform.apisetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.test.platform.exception.BusinessException;
import com.test.platform.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//这个注解是指这个类是处理其他controller抛出的异常
@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> defaultExceptionHandler(HttpServletRequest request, RuntimeException ex) {
		log.debug(String.valueOf(request));
		log.error("错误的请求", ex);

		return new ResponseEntity<>(ApiRes.error(ErrorCode.SYSTEM_ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> MethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		List<Map<String,Object>> messages = new ArrayList<>();
		ex.getBindingResult().getFieldError();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			Map<String,Object> map = new  HashMap<>();
			String message = error.getDefaultMessage();
			String field = error.getField();
			map.put("field",field);
			map.put("message",message);
			messages.add(map);
		}
		return new ResponseEntity<>(ApiRes.error(ErrorCode.SYSTEM_ERROR,messages), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> businessException(BusinessException ex){
		int error = ex.getErrorCode();
		String description = ex.getMessage();
		log.error(description,ex);
		ResponseEntity<Object> responseEntity = new ResponseEntity<>(ApiRes.error(error, description), HttpStatus.BAD_REQUEST);
		return responseEntity;
	}
}
