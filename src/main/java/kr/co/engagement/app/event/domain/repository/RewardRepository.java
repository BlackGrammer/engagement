package kr.co.engagement.app.event.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.app.event.domain.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findAllByEventIsAndRegDateBetween(Event event, LocalDateTime start, LocalDateTime end);
    Optional<Reward> findByEventIsAndMemberIdIsAndRegDateBetween(Event event, long memberId, LocalDateTime start, LocalDateTime end);
}
