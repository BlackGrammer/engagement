package kr.co.engagement.app.event.application;

import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.engagement.app.event.application.dto.EventDto;
import kr.co.engagement.app.event.application.dto.RewardIssueDto;
import kr.co.engagement.app.event.application.exception.EventException;
import kr.co.engagement.app.event.application.mapper.EventMapper;
import kr.co.engagement.app.event.application.mapper.RewardMapper;
import kr.co.engagement.app.event.domain.RewardProvider;
import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.repository.EventRepository;
import kr.co.engagement.app.event.domain.repository.RewardRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[UNIT] EventService")
class EventServiceTest {

    @InjectMocks
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @Mock
    RewardRepository rewardRepository;

    @Mock
    RewardProvider rewardProvider;

    @Mock
    EventMapper eventMapper;

    @Mock
    RewardMapper rewardMapper;

    @Test
    @DisplayName("보상 정보 조회시 보상DTO 반환성공")
    void findReward() {
        // given
        Event entity = mock(Event.class);
        EventDto dto = mock(EventDto.class);
        String givenId = "reward";
        given(eventRepository.findById(anyString())).willReturn(Optional.of(entity));
        given(eventMapper.toDto(entity)).willReturn(dto);

        //when
        eventService.findOne(givenId);

        //then
        then(eventRepository).should(atLeastOnce()).findById(givenId);
        then(eventMapper).should(atLeastOnce()).toDto(entity);
    }

    @Test
    @DisplayName("보상 정보 존재 안할 시 EVENT_NOT_FOUND 에러 발생")
    void failToFindEvent() {
        // given
        String givenId = "reward";
        given(eventRepository.findById(givenId)).willReturn(Optional.empty());

        //when
        when(() -> eventService.findOne(givenId));

        //then
        BDDAssertions.then(caughtException()).isInstanceOf(EventException.class);
        then(eventRepository).should(atLeastOnce()).findById(givenId);
        then(eventMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("보상지급 요청시 발급 성공")
    void issueReward() {
        // given
        String eventId = "test";
        long memberId = 1L;
        Reward mockReward = mock(Reward.class);
        RewardIssueDto mockDto = mock(RewardIssueDto.class);
        given(rewardProvider.createReward(eventId, memberId)).willReturn(mockReward);
        given(rewardRepository.save(mockReward)).willReturn(mockReward);
        given(rewardMapper.toDto(mockReward)).willReturn(mockDto);

        //when
        RewardIssueDto actual = eventService.issueReward(eventId, memberId);

        //then
        assertThat(actual, equalTo(mockDto));
    }

}