package kr.co.engagement.app.event.infrastructure;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.config.BootstrapMode;

import kr.co.engagement.app.event.application.dto.RewardDailyDto;
import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.app.member.domain.entity.Member;
import kr.co.engagement.core.config.jpa.JpaConfig;

@DataJpaTest(bootstrapMode = BootstrapMode.LAZY)
@Import({JpaConfig.class, RewardQueryRepository.class})
@DisplayName("[UNIT] RewardQueryRepository")
class RewardQueryRepositoryTest {

    @Autowired
    RewardQueryRepository rewardQueryRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("날짜조건으로 보상지급내역 조회시 전체 조회성공")
    void findRewardByDate() {
        // given
        Member firstUser = Member.builder()
            .memberName("test user1")
            .build();
        entityManager.persist(firstUser);

        Member secondUser = Member.builder()
            .memberName("test user2")
            .build();
        entityManager.persist(secondUser);

        String eventId = "test id";
        Event event = Event.builder()
            .id(eventId)
            .title("test title")
            .description("test desc")
            .eventType(EventType.CONTINUOUS_REWARD)
            .build();

        event.addReward(Reward.builder()
            .memberId(firstUser.getId())
            .point(1000)
            .seq(1)
            .build());

        event.addReward(Reward.builder()
            .memberId(secondUser.getId())
            .point(1000)
            .seq(1)
            .build());

        entityManager.persist(event);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "regDate"));

        // when
        Page<RewardDailyDto> actual = rewardQueryRepository.findRewardByDate(eventId, LocalDate.now(), pageable);

        // then
        assertThat(actual.getContent().size(), equalTo(2));
    }
}