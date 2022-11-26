package kr.co.engagement.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenValidator jwtTokenValidator;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests(
                (auth) -> auth
                    .antMatchers(HttpMethod.POST, "/api/members/authorize").permitAll()
                    .antMatchers(HttpMethod.GET, "/", "/api/events/**/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenValidator),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
