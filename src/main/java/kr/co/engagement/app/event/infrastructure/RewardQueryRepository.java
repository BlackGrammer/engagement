package kr.co.engagement.app.event.infrastructure;

import static kr.co.engagement.app.event.domain.entity.QEvent.*;
import static kr.co.engagement.app.event.domain.entity.QReward.*;
import static kr.co.engagement.app.member.domain.entity.QMember.*;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;

import kr.co.engagement.app.event.application.dto.QRewardDailyDto;
import kr.co.engagement.app.event.application.dto.RewardDailyDto;
import kr.co.engagement.app.event.domain.entity.Reward;
import kr.co.engagement.core.util.Querydsl4RepositorySupport;

@Repository
public class RewardQueryRepository extends Querydsl4RepositorySupport {

    public RewardQueryRepository() {
        super(Reward.class);
    }

    /**
     * 특정 날짜의 보상지급 내역목록 반환
     *
     * @param eventId  이벤트 ID
     * @param date     조회날짜
     * @param pageable 페이지/정렬조건
     * @return 보상지급 내역
     */
    public Page<RewardDailyDto> findRewardByDate(String eventId, LocalDate date, Pageable pageable) {
        return applyPagination(pageable, jpaQueryFactory ->
            jpaQueryFactory.select(new QRewardDailyDto(
                    reward.id,
                    reward.event.title,
                    member.id,
                    member.memberName,
                    reward.point,
                    reward.seq,
                    reward.regDate
                ))
                .from(reward)
                .innerJoin(reward.event, event)
                .innerJoin(member).on(member.id.eq(reward.memberId))
                .where(
                    issuedAt(date),
                    eventIdEq(eventId)
                )
        );
    }

    private BooleanExpression issuedAt(LocalDate date) {
        return date == null ? null : reward.regDate.between(
            date.atStartOfDay(),
            date.plusDays(1).atStartOfDay()
        );
    }

    private BooleanExpression eventIdEq(String eventId) {
        return eventId == null ? null : reward.event.id.eq(eventId);
    }

}
