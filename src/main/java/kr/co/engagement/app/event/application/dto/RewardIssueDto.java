package kr.co.engagement.app.event.application.dto;

import java.io.Serializable;

import kr.co.engagement.app.event.domain.enums.EventType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardIssueDto implements Serializable {
    private int point;
    private int seq;
    private String eventTitle;
    private EventType eventType;
}