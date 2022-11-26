package kr.co.engagement.app.event.domain.strategy;

import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.enums.EventType;

/**
 * 이벤트 유형에 따른 보상지급 전략 인터페이스
 *
 * @see EventType
 */
public interface RewardStrategy {
    /**
     * 관련 이벤트 유형 반환
     *
     * @return 이벤트 유형
     */
    EventType getEventType();

    /**
     * 보상지급 엔티티 생성
     *
     * @param eventId  이벤트 ID
     * @param memberId 유저 ID
     * @return 보상지급 엔티티
     */
    Reward createReward(String eventId, long memberId);
}
