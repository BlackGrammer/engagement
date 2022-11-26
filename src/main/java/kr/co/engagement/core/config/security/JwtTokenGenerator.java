package kr.co.engagement.core.config.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
public class JwtTokenGenerator {

    private final long ACCESS_TTL;
    private final JwtSignKeyHolder jwtSignKeyHolder;

    public JwtTokenGenerator(JwtSignKeyHolder jwtSignKeyHolder, @Value("${jwt.access-ttl}") long ACCESS_TTL) {
        this.jwtSignKeyHolder = jwtSignKeyHolder;
        this.ACCESS_TTL = ACCESS_TTL;
    }

    public String generateToken(long id) {
        Date now = Date.from(Instant.now());
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setSubject(String.valueOf(id))
            .setIssuer("engagement")
            .setIssuedAt(now)
            .setExpiration(Date.from(now.toInstant().plus(ACCESS_TTL, ChronoUnit.HOURS)))
            .setId(UUID.randomUUID().toString())
            .signWith(jwtSignKeyHolder.getAccessSignKey(), SignatureAlgorithm.HS256)
            .compact();
    }

}
