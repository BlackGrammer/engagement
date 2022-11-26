package kr.co.engagement.app.event.application.exception;

import org.springframework.http.HttpStatus;

import kr.co.engagement.core.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements BaseErrorCode {
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR-EVT-404-001", "존재하지 않는 이벤트 입니다."),
    REWARD_LIMIT_EXCEED(HttpStatus.FORBIDDEN, "ERR-EVT-403-001", "오늘의 보상지급이 마감되었습니다. 내일 다시 도전해보세요."),
    REWARD_ALREADY_ISSUE(HttpStatus.FORBIDDEN, "ERR-EVT-403-002", "이미 보상지급이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
