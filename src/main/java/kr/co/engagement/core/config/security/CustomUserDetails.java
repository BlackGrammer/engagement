package kr.co.engagement.core.config.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import kr.co.engagement.app.member.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomUserDetails extends User {

    private static final String SIMPLE_USER_ROLE = "USER";

    public CustomUserDetails(String memberId) {
        super(memberId, memberId, Collections.singletonList(new SimpleGrantedAuthority(SIMPLE_USER_ROLE)));
    }
}
