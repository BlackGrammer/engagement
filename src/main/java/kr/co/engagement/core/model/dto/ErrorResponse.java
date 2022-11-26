package kr.co.engagement.core.model.dto;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import kr.co.engagement.core.exception.BaseErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse implements Serializable {

    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(BaseErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());

    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.name(), message);
    }
}