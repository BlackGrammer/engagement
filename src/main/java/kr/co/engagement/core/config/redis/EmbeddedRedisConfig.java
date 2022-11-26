package kr.co.engagement.core.config.redis;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@Primary
@Configuration
public class EmbeddedRedisConfig extends RedissonAutoConfiguration implements InitializingBean, DisposableBean {
    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisServer = RedisServer.newRedisServer().port(redisPort).build();
        redisServer.start();
        log.info("--- embedded redis started");
    }

    @Override
    public void destroy() throws Exception {
        redisServer.stop();
        log.info("--- embedded redis shutdown complete");
    }
}