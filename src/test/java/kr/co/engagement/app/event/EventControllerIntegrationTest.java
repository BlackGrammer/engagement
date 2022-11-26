package kr.co.engagement.app.event;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.repository.EventRepository;
import kr.co.engagement.app.member.application.dto.AuthorizeDto;
import kr.co.engagement.core.config.security.JwtTokenGenerator;
import kr.co.engagement.core.model.dto.ResponseModel;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("[INTEGRATION] API 통합테스트 (보상조회, 보상지급내역조회, 보상발급요청)")
public class EventControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    EventRepository eventRepository;

    @Value("${jwt.token-type}")
    private String TOKEN_TYPE;

    @Test
    @DisplayName("'reward' 보상 조회시 조회 ID 와 일치하는 데이터 반환")
    public void getEventInfo() throws Exception {
        // given
        String eventId = "reward";

        //when
        ResultActions result = mockMvc
            .perform(
                get("/api/events/{eventId}", eventId)
            );

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(is(eventId)))
            .andDo(print());
    }

    @Test
    @DisplayName("'reward' 보상 지급 내역 조회시 data 와 page 정보 일치")
    public void getRewardList() throws Exception {
        // given
        String eventId = "reward";
        String date = "2022-11-23";

        // when
        ResultActions result = mockMvc
            .perform(
                get("/api/events/{eventId}/rewards", eventId)
                    .param("date", date)
                    .param("direction", "DESC")
            );

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()", is(10)))
            .andDo(print());
    }

    @Test
    @DisplayName("'reward' 보상지급 동시요청시 정해진 인원만 보상지급")
    public void rewardIssueConcurrencyTest() throws Exception {
        // given
        String eventId = "reward";
        Event event = eventRepository.findById(eventId).orElseThrow();
        int dailyLimit = event.getDailyLimit();

        int requestCnt = 15;
        ConcurrentLinkedQueue<HttpHeaders> tokenHeaderList = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < requestCnt; i++) {
            // 토큰 생성
            tokenHeaderList.add(getAccessToken(i));
        }

        // when
        ExecutorService service = Executors.newFixedThreadPool(requestCnt);
        CountDownLatch latch = new CountDownLatch(requestCnt);
        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();
        for (HttpHeaders header : tokenHeaderList) {
            /// 동시 발급 요청
            service.execute(() -> {
                try {
                    MvcResult mvcResult = mockMvc
                        .perform(
                            post("/api/events/{eventId}/rewards", eventId)
                                .headers(header)
                        )
                        .andReturn();
                    int status = mvcResult.getResponse().getStatus();
                    if (status == HttpStatus.OK.value()) {
                        successCnt.incrementAndGet();
                    } else {
                        failCnt.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCnt.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        assertThat(successCnt.intValue(), equalTo(dailyLimit));
        assertThat(failCnt.intValue(), equalTo(requestCnt - dailyLimit));
    }


    // 유저 토큰 생성
    private HttpHeaders getAccessToken(long memberId) {
        String generateToken = jwtTokenGenerator.generateToken(memberId);
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + generateToken);

        return authHeaders;
    }

}
