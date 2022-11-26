package kr.co.engagement.app.event.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.engagement.app.event.application.exception.EventErrorCode;
import kr.co.engagement.app.event.application.exception.EventException;
import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.app.event.domain.repository.EventRepository;
import kr.co.engagement.app.event.domain.strategy.RewardStrategy;

@Component
public class RewardProvider {

    private final Map<EventType, RewardStrategy> strategyMap;

    private final EventRepository eventRepository;

    @Autowired
    public RewardProvider(Set<RewardStrategy> strategySet, EventRepository eventRepository) {
        strategyMap = new HashMap<>();
        strategySet.forEach(rewardStrategy -> strategyMap.put(rewardStrategy.getEventType(), rewardStrategy));
        this.eventRepository = eventRepository;
    }

    /**
     * 새로 지급할 보상 생성
     *
     * @param eventId  이벤트 ID
     * @param memberId 유저 ID
     * @return 새로운 보상
     * @see RewardStrategy
     */
    public Reward createReward(String eventId, long memberId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EventException(EventErrorCode.EVENT_NOT_FOUND));

        return strategyMap.get(event.getEventType()).createReward(eventId, memberId);
    }
}
