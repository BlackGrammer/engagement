package kr.co.engagement.app.event.application.mapper;

import org.mapstruct.Mapper;

import kr.co.engagement.app.event.domain.entity.Event;
import kr.co.engagement.core.util.BaseMapper;
import kr.co.engagement.app.event.application.dto.EventDto;

@Mapper
public interface EventMapper extends BaseMapper<Event, EventDto> {
}
