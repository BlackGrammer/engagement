package kr.co.engagement.app.event.presentation;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.engagement.app.event.application.EventService;
import kr.co.engagement.app.event.application.dto.EventDto;
import kr.co.engagement.app.event.application.dto.RewardDailyDto;
import kr.co.engagement.app.event.application.dto.RewardIssueDto;
import kr.co.engagement.app.event.application.dto.RewardSearchCondition;
import kr.co.engagement.core.config.web.TokenSubject;
import kr.co.engagement.core.model.dto.ResponseModel;
import kr.co.engagement.core.model.dto.ResponsePageModel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseModel<EventDto> one(@PathVariable String eventId) {

        EventDto eventDto = eventService.findOne(eventId);
        return ResponseModel.ok(eventDto);
    }

    @GetMapping("/{eventId}/rewards")
    public ResponsePageModel<RewardDailyDto> rewardList(@PathVariable String eventId, @Valid RewardSearchCondition condition) {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(condition.getDirection(), "regDate"));
        Page<RewardDailyDto> rewardList = eventService.findRewardList(eventId, condition.getDate(), pageable);
        return ResponsePageModel.ok(rewardList);
    }

    @PostMapping("/{eventId}/rewards")
    public ResponseModel<RewardIssueDto> issueReward(@PathVariable String eventId, @TokenSubject long memberId) {

        RewardIssueDto rewardIssueDto = eventService.issueReward(eventId, memberId);
        return ResponseModel.ok(rewardIssueDto);
    }
}
