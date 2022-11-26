package kr.co.engagement.core.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "ERR-CM-400-001", "요청정보를 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "ERR-CM-404-001", "요청하신 페이지를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERR-CM-500-001", "알 수 없는 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
