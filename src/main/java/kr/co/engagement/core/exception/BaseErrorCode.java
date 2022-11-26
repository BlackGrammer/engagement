package kr.co.engagement.core.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

	HttpStatus getHttpStatus();
	String getCode();
	String getMessage();
}