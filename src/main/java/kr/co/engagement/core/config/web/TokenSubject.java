package kr.co.engagement.core.config.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * access token 의 subject 정보 추출시 사용
 *
 * @see TokenSubjectArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenSubject {
}
