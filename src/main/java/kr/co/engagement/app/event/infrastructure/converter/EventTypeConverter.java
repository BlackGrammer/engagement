package kr.co.engagement.app.event.infrastructure.converter;

import javax.persistence.Converter;

import kr.co.engagement.app.event.domain.enums.EventType;
import kr.co.engagement.core.util.CodeEnumConverter;

@Converter(autoApply = true)
public class EventTypeConverter extends CodeEnumConverter<EventType, String> { }