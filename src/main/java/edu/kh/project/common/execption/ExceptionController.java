package edu.kh.project.common.execption;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 예외처리용 어노테이션
public class ExceptionController {

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e) {
		e.printStackTrace(); // 에러 내용 콘솔에 출력
		return "error/500"; // /template/error/500.html
	}
	
}
