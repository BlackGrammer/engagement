package kr.co.engagement.core.config.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    @Value("${jwt.token-type}")
    private String TOKEN_TYPE;
    private final CustomUserDetailsService userDetailsService;
    private final JwtSignKeyHolder jwtSignKeyHolder;

    public boolean isValidTokenHeader(String authHeader) {
        if (!StringUtils.hasLength(authHeader) || !authHeader.startsWith(TOKEN_TYPE)) {
            return false;
        }

        String token = getTokenFromHeader(authHeader);
        return !isTokenExpired(getTokenClaims(token));
    }

    public String getTokenFromHeader(String header) {
        return header.replace(TOKEN_TYPE + " ", "");
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(Date.from(Instant.now()));
    }

    public Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtSignKeyHolder.getAccessSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String getTokenSubject(String token) {
        return getTokenClaims(token).getSubject();
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenSubject(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
