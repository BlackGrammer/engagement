package kr.co.engagement.app.event.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import kr.co.engagement.app.event.application.EventService;
import kr.co.engagement.app.event.application.dto.EventDto;
import kr.co.engagement.app.event.application.dto.RewardDailyDto;
import kr.co.engagement.app.event.application.exception.EventErrorCode;
import kr.co.engagement.app.event.application.exception.EventException;
import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.core.config.security.JwtTokenValidator;
import kr.co.engagement.core.config.web.WebConfig;

@WebMvcTest(
    value = EventController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@DisplayName("[UNIT] EventController")
class EventControllerTest {

    @MockBean
    EventService eventService;

    @MockBean
    JwtTokenValidator jwtTokenValidator;

    @Autowired
    MockMvc mockMvc;




    @Test
    @DisplayName("reward 이벤트 조회시 결과 반환")
    void findOne() throws Exception {
        // given
        String givenId = "reward";
        EventDto dto = EventDto.builder()
            .id(givenId)
            .title("title")
            .description("description")
            .eventType(EventType.CONTINUOUS_REWARD)
            .dailyLimit(10)
            .build();

        given(eventService.findOne(anyString())).willReturn(dto);

        // when
        ResultActions resultActions = mockMvc
            .perform(get("/api/events/{eventId}", givenId))
            .andDo(print());

        //then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(is(givenId)));
    }

    @Test
    @DisplayName("존재하지 않는 이벤트 페이지 진입시 오류반환")
    void failToFindOne() throws Exception {
        // given
        EventErrorCode givenErrorCode = EventErrorCode.EVENT_NOT_FOUND;
        given(eventService.findOne(anyString())).willThrow(new EventException(givenErrorCode));

        // when
        ResultActions resultActions = mockMvc
            .perform(get("/api/events/{eventId}", "nowhere"))
            .andDo(print());

        //then
        resultActions
            .andExpect(status().is(givenErrorCode.getHttpStatus().value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(is(givenErrorCode.getCode())));
    }

    @Test
    @DisplayName("특정날짜의 보상지급내역 조회시 결과 반환")
    void findRewardList() throws Exception {
        // given
        Page<RewardDailyDto> expected = new PageImpl<>(
            List.of(
                new RewardDailyDto(1, "test event", 1, "test user1", 1_000, 1, LocalDateTime.now()),
                new RewardDailyDto(2, "test event", 2, "test user2", 1_300, 1, LocalDateTime.now())
            ));

        given(eventService.findRewardList(anyString(), any(), any())).willReturn(expected);

        // when
        ResultActions resultActions = mockMvc
            .perform(
                get("/api/events/{eventId}/rewards", "testId")
                    .param("date", "2022-11-23")
                    .param("direction", "DESC")
            )
            .andDo(print());

        //then
        resultActions.andExpect(status().isOk());
    }
}