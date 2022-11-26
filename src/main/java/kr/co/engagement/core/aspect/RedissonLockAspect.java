package kr.co.engagement.core.aspect;

import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 특정 키에 대한 분산 lock 제어 Aspect
 *
 * @see RedissonLock
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redisson;
    private final ProxyTransaction proxyTransaction;

    @Around("@annotation(kr.co.engagement.core.aspect.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        RedissonLock annotation = signature.getMethod().getAnnotation(RedissonLock.class);

        // RedissonLock 애노테이션 정보 탐색
        String key = annotation.key();
        long waitTime = annotation.waitTime();
        long leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();

        // 메서드 parameter 값 탐색
        Parameter[] parameters = signature.getMethod().getParameters();
        String keyValue = "none";
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(key)) {
                keyValue = String.valueOf(joinPoint.getArgs()[i]);
            }
        }

        // redisson lock -> proxy tx start -> proceed -> tx commit -> redisson unlock
        RLock lock = redisson.getLock(String.format("%s:%s", key, keyValue));
        Object result = null;
        if (lock.tryLock(waitTime, leaseTime, timeUnit)) {
            try {
                result = proxyTransaction.proceed(joinPoint);
            } finally {
                lock.unlock();
            }
        }

        return result;
    }
}