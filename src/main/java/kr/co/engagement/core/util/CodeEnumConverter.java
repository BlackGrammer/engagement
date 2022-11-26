package kr.co.engagement.core.util;

import java.lang.reflect.ParameterizedType;

import javax.persistence.AttributeConverter;

import kr.co.engagement.core.model.superclass.CodeEnum;

/**
 * 영속화가 필요한 Enum 필드를 엔티티 <-> DB값 변환 해주는 인터페이스
 *
 * <p>
 * CodeEnum 을 구현한 Enum 의 경우 상속을 받아 @Converter 애노테이션만 붙여서 사용하면 동작
 * </p>
 * @param <T> Enum type
 * @param <E> CodeEnum 의 code 타입
 */
public abstract class CodeEnumConverter<T extends Enum<?> & CodeEnum<E>, E> implements AttributeConverter<T, E> {
    private final Class<T> enumClass;

    public CodeEnumConverter() {
        enumClass = detectAttributeType();
    }

    @Override
    public E convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(E dbData) {

        return CodeEnumHelper.fromCode(enumClass, dbData);
    }

    private Class<T> detectAttributeType() {
        ParameterizedType type = (ParameterizedType)getClass().getGenericSuperclass();
        return (Class<T>)type.getActualTypeArguments()[0];
    }
}
