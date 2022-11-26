package kr.co.engagement.app.event.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardDailyDto implements Serializable {
    private long id;
    private String eventTitle;
    private long memberId;
    private String memberName;
    private int point;
    private int seq;

    private LocalDateTime issuedAt;

    @QueryProjection
    public RewardDailyDto(long id, String eventTitle, long memberId, String memberName, int point, int seq, LocalDateTime issuedAt) {
        this.id = id;
        this.eventTitle = eventTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.point = point;
        this.seq = seq;
        this.issuedAt = issuedAt;
    }
}
