package com.team6.hanghaesisters.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final String msg;
	private final int statusCode;

	public CustomException (ErrorCode errorCode) {
		this.msg = errorCode.getMsg();
		this.statusCode = errorCode.getStatusCode();
	}
}
