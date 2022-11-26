package kr.co.engagement.core.util;

import java.util.List;

/**
 * Entity <-> Dto 변환시 사용할 매퍼 인터페이스
 *
 * <p>세부조절이 필요한 경우 구현체에서 정의 필요</p>
 *
 * @param <E> Entity
 * @param <D> Dto
 */
public interface BaseMapper<E, D> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityList(List<D> dto);

    List<D> toDtoList(List<E> dto);
}
