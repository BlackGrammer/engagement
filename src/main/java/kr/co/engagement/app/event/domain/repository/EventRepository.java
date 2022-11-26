package kr.co.engagement.app.event.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.engagement.app.event.domain.entity.Event;

public interface EventRepository extends JpaRepository<Event, String> {
}
