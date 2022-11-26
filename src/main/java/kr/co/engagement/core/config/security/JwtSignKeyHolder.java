package kr.co.engagement.core.config.security;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtSignKeyHolder {
    private final Key ACCESS_JWT_SIGN_KEY;

    public JwtSignKeyHolder(@Value("${jwt.access-secret}") String accessSecret) {
        this.ACCESS_JWT_SIGN_KEY = Keys.hmacShaKeyFor(accessSecret.getBytes());
    }

    public Key getAccessSignKey() {
        return this.ACCESS_JWT_SIGN_KEY;
    }
}
