package kr.co.engagement.app.event.domain.strategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.co.engagement.app.event.application.exception.EventErrorCode;
import kr.co.engagement.app.event.application.exception.EventException;
import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.app.event.domain.repository.EventRepository;
import kr.co.engagement.app.event.domain.repository.RewardRepository;
import lombok.RequiredArgsConstructor;

/**
 * 연속보상 유형 이벤트에 대한 보상지급 전략
 * <p>{@link EventType#CONTINUOUS_REWARD}</p>
 * @
 */
@Component
@RequiredArgsConstructor
public class ContinuousRewardStrategy implements RewardStrategy {

    private final int DEFAULT_POINT = 1_000;

    private final EventRepository eventRepository;
    private final RewardRepository rewardRepository;

    @Override
    public EventType getEventType() {
        return EventType.CONTINUOUS_REWARD;
    }

    /**
     * 연속보상 유형의 보상지급 엔티티 생성
     * <pre>
     * 1. 보상지급이 초과하면 지급 불가
     * 2. 동일 유저가 같은날 다시 발급 불가
     * 3. 연속보상에 따라 추가 포인트 부여 {@link ContinuousRewardStrategy#calculatePoint}
     * </pre>
     *
     * @param eventId  이벤트 ID
     * @param memberId 유저 ID
     * @return 보상지급 엔티티
     */
    @Override
    public Reward createReward(String eventId, long memberId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EventException(EventErrorCode.EVENT_NOT_FOUND));

        // 보상지급 초과여부 확인
        checkRewardLimitExceeded(event);
        // 유저가 이미 보상받았는지 여부 확인
        checkAlreadyIssued(event, memberId);

        // 이번 지급할 보상을 결정하여 반환
        int seq = getPrevRewardSeq(event, memberId) + 1;
        int point = calculatePoint(seq);

        return Reward.builder()
            .seq(seq)
            .point(point)
            .event(event)
            .memberId(memberId)
            .build();
    }

    /**
     * 포인트 계산
     * <pre>
     * 1. 기본포인트 : 1000
     * 2. 3/5/10일 연속: 1000 + (연속일자 * 100)
     * </pre>
     *
     * @param seq 연속일자 차수
     * @return 보상할 포인트
     */
    private int calculatePoint(int seq) {
        switch (seq) {
            case 3:
            case 5:
            case 10:
                return DEFAULT_POINT + seq * 100;
            default:
                return DEFAULT_POINT;
        }
    }

    /**
     * 이벤트에 설정된 일일한도 값을 넘는 보상지급내역이 존재하는지 확인합니다.
     *
     * @param event 이벤트 정보
     * @throws EventException 한도를 다채워 지급이 불가
     *                        {@link EventErrorCode#REWARD_LIMIT_EXCEED}
     */
    private void checkRewardLimitExceeded(Event event) {
        List<Reward> todayRewardList = rewardRepository.findAllByEventIsAndRegDateBetween(
            event,
            LocalDate.now().atStartOfDay(),
            LocalDate.now().plusDays(1).atStartOfDay());

        if (todayRewardList.size() >= event.getDailyLimit()) {
            throw new EventException(EventErrorCode.REWARD_LIMIT_EXCEED);
        }
    }

    /**
     * 특정 유저가 해당 이벤트에 당일 보상지급을 받았는지 확인합니다.
     *
     * @param event    이벤트정보
     * @param memberId 유저 ID
     * @throws EventException 당일 이미 발급받은 유저
     *                        {@link EventErrorCode#REWARD_ALREADY_ISSUE}
     */
    private void checkAlreadyIssued(Event event, long memberId) {
        Optional<Reward> issuedReward = rewardRepository.findByEventIsAndMemberIdIsAndRegDateBetween(
            event,
            memberId,
            LocalDate.now().atStartOfDay(),
            LocalDate.now().plusDays(1).atStartOfDay()
        );

        if (issuedReward.isPresent()) {
            throw new EventException(EventErrorCode.REWARD_ALREADY_ISSUE);
        }
    }

    /**
     * 특정유저의 전일 연속보상 횟수를 조회합니다.
     * <pre>
     * 1. 존재하지 않는다면 0
     * 2. 10 이상이라면 0 으로 초기화 (10일 이후 연속보상 초기화)
     * </pre>
     *
     * @param event    이벤트정보
     * @param memberId 유저 ID
     * @return 전일 보상지급내역
     */
    private int getPrevRewardSeq(Event event, long memberId) {
        Optional<Reward> prevReward = rewardRepository.findByEventIsAndMemberIdIsAndRegDateBetween(
            event,
            memberId,
            LocalDate.now().minusDays(1).atStartOfDay(),
            LocalDate.now().atStartOfDay()
        );
        int seq = prevReward.map(Reward::getSeq).orElse(0);

        return seq < 10 ? seq : 0;
    }

}
