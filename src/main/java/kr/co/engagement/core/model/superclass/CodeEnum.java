package kr.co.engagement.core.model.superclass;

import com.fasterxml.jackson.annotation.JsonValue;

public interface CodeEnum<E> {
	@JsonValue
	E getCode();
}
