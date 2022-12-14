package kr.co.engagement.core.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {
    String key();

    long waitTime() default 3L;

    long leaseTime() default 1L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
