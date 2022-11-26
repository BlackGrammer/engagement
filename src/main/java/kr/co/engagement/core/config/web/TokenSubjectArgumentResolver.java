package kr.co.engagement.core.config.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import kr.co.engagement.core.config.security.JwtTokenValidator;
import lombok.RequiredArgsConstructor;

/**
 * access token 정보에서 subject 를 추출하여 {@link TokenSubject} 애노테이션 파라미터에 할당
 */
@Component
@RequiredArgsConstructor
public class TokenSubjectArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        // TokenSubject 애노테이션이 붙은 long 원시/래퍼타입 변수에만 할당
        return parameter.hasParameterAnnotation(TokenSubject.class)
            && (long.class.isAssignableFrom(parameterType)
            || Long.class.isAssignableFrom(parameterType));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String tokenFromHeader = jwtTokenValidator.getTokenFromHeader(header);
        String tokenSubject = jwtTokenValidator.getTokenSubject(tokenFromHeader);

        return Long.parseLong(tokenSubject);
    }
}
