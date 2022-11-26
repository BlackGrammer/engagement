package kr.co.engagement.core.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

import kr.co.engagement.core.model.superclass.CodeEnum;

public class CodeEnumHelper {

    /**
     * CodeEnum 의 code 값으로 부터 Enum 의 value 추출
     *
     * @param codeEnum Enum Class
     * @param code     code 값
     * @param <T>      Enum Type
     * @param <E>      code Type
     * @return Enum Value
     */
    public static <T extends Enum<?> & CodeEnum<E>, E> T fromCode(Class<T> codeEnum, E code) {
        return Arrays.stream(codeEnum.getEnumConstants())
            .filter(e -> e.getCode().equals(code))
            .findAny()
            .orElseThrow(NoSuchElementException::new);
    }
}