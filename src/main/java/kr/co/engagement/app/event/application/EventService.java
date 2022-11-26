package kr.co.engagement.app.event.application;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.engagement.app.event.application.dto.EventDto;
import kr.co.engagement.app.event.application.dto.RewardDailyDto;
import kr.co.engagement.app.event.application.dto.RewardIssueDto;
import kr.co.engagement.app.event.application.exception.EventErrorCode;
import kr.co.engagement.app.event.application.exception.EventException;
import kr.co.engagement.app.event.application.mapper.EventMapper;
import kr.co.engagement.app.event.application.mapper.RewardMapper;
import kr.co.engagement.app.event.domain.RewardProvider;
import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.repository.EventRepository;
import kr.co.engagement.app.event.domain.repository.RewardRepository;
import kr.co.engagement.app.event.infrastructure.RewardQueryRepository;
import kr.co.engagement.core.aspect.RedissonLock;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final RewardQueryRepository rewardQueryRepository;
    private final EventMapper eventMapper;
    private final RewardMapper rewardMapper;
    private final RewardRepository rewardRepository;
    private final RewardProvider rewardProvider;

    /**
     * 이벤트 정보 조회
     *
     * @param eventId 이벤트 ID
     * @return 이벤트 정보
     */
    public EventDto findOne(String eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new EventException(EventErrorCode.EVENT_NOT_FOUND));

        return eventMapper.toDto(event);
    }

    /**
     * 보상지급 목록 조회
     *
     * @param eventId  이벤트 ID
     * @param date     지급날짜
     * @param pageable 페이징,정렬 조건
     * @return 보상 목록
     */
    public Page<RewardDailyDto> findRewardList(String eventId, LocalDate date, Pageable pageable) {
        return rewardQueryRepository.findRewardByDate(eventId, date, pageable);
    }

    /**
     * 새로운 보상지급
     * <p>
     * 보상지급 가능여부 및 포인트는 각 보상유형에 따라 결정
     * </p>
     *
     * @param eventId  이벤트 ID
     * @param memberId 유저 ID
     * @return 보상지급 정보
     * @see RewardProvider
     */
    @RedissonLock(key = "eventId", leaseTime = 3L, waitTime = 10L)
    public RewardIssueDto issueReward(String eventId, Long memberId) {
        Reward reward = rewardProvider.createReward(eventId, memberId);
        Reward saved = rewardRepository.save(reward);
        return rewardMapper.toDto(saved);
    }

}
