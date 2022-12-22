package com.team6.hanghaesisters.exception;

import com.team6.hanghaesisters.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

	//Custom Exception
	@ExceptionHandler(value = {CustomException.class})
	@ResponseStatus(code = HttpStatus.OK)
	public ErrorResponseDto handleCustomException(CustomException ex) {
		log.error("custom exception occurred: " + ex.getMsg());

		return new ErrorResponseDto(ex.getMsg(), ex.getStatusCode());
	}


	//Valid Exception
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
		log.error("validation exception occurred: " + ex.getMessage());

		BindingResult bindingResult = ex.getBindingResult();
		StringBuilder sb = new StringBuilder();
		bindingResult.getAllErrors()
			.forEach(objectError -> sb.append(objectError.getDefaultMessage()));
		return new ErrorResponseDto(sb.toString(), HttpStatus.BAD_REQUEST.value());
	}


}