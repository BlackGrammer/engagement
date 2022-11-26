package kr.co.engagement.app.event.application.exception;

import kr.co.engagement.core.exception.BaseException;

public class EventException extends BaseException {

    public EventException(EventErrorCode errorCode) {
        super(errorCode);
    }
}
