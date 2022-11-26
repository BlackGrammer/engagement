package kr.co.engagement.app.event.domain.enums;

import kr.co.engagement.core.model.superclass.CodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType implements CodeEnum<String> {

    /** 연속보상 유형
     * @see kr.co.engagement.app.event.domain.strategy.ContinuousRewardStrategy
     * */
    CONTINUOUS_REWARD("CONT");

    private final String code;
}
