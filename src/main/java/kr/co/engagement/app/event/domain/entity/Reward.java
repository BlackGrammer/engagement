package kr.co.engagement.app.event.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.co.engagement.core.model.superclass.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false)
    private long memberId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Builder
    public Reward(int point, int seq, long memberId, Event event) {
        this.point = point;
        this.seq = seq;
        this.memberId = memberId;
        this.event = event;
    }
}
