package kr.co.engagement.app.event.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.core.model.superclass.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private int dailyLimit;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Reward> rewardList;

    @Builder
    public Event(String id, String title, String description, EventType eventType, int dailyLimit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventType = eventType;
        this.dailyLimit = dailyLimit;
        this.rewardList = new ArrayList<>();
    }

    public void addReward(Reward reward) {
        this.rewardList.add(reward);
        reward.setEvent(this);
    }
}
