package kr.co.engagement.app.event.domain.repository;

import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.core.config.jpa.JpaConfig;

@DataJpaTest
@Import({JpaConfig.class})
@DisplayName("[UNIT] RewardRepository")
class RewardRepositoryTest {

    @Autowired
    RewardRepository eventRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("이벤트의 지급된 보상 목록 날짜조건으로 조회")
    void findAllByEventIsAndRegDateBetween() {
        // given
        Event event = entityManager.persist(
            Event.builder()
                .id("TEST")
                .title("title")
                .description("desc")
                .eventType(EventType.CONTINUOUS_REWARD)
                .dailyLimit(10)
                .build());

        int persistSize = 2;
        for (int i = 0; i < persistSize; i++) {
            entityManager.persist(Reward.builder()
                .event(event)
                .memberId(1)
                .point(1_000)
                .seq(1)
                .build());
        }

        // when
        LocalDate now = LocalDate.now();
        List<Reward> actual = eventRepository.findAllByEventIsAndRegDateBetween(
            event,
            now.atStartOfDay(),
            now.plusDays(1).atStartOfDay()
        );

        // then
        assertThat(actual.size(), equalTo(persistSize));
    }

    @Test
    @DisplayName("이벤트의 특정 유저에게 지급된 보상 목록 날짜 조건으로 조회")
    void findByEventIsAndMemberIdIsAndRegDateBetween() {
        // given
        Event event = entityManager.persist(
            Event.builder()
                .id("TEST")
                .title("title")
                .description("desc")
                .eventType(EventType.CONTINUOUS_REWARD)
                .dailyLimit(10)
                .build());

        long memberId = 1L;
        entityManager.persist(Reward.builder()
            .event(event)
            .memberId(memberId)
            .point(1_000)
            .seq(1)
            .build());

        // when
        LocalDate now = LocalDate.now();
        when(() -> eventRepository.findByEventIsAndMemberIdIsAndRegDateBetween(
                    event,
                    memberId,
                    now.atStartOfDay(),
                    now.plusDays(1).atStartOfDay()
                )
                .orElseThrow()
        );

        // then
        BDDAssertions.then(caughtException()).isNull();
    }
}