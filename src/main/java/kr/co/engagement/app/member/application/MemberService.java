package kr.co.engagement.app.member.application;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.engagement.app.member.application.dto.AuthorizeDto;
import kr.co.engagement.app.member.domain.entity.Member;
import kr.co.engagement.app.member.domain.repository.MemberRepository;
import kr.co.engagement.core.config.security.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    /**
     * 유저정보를 토대로 access token 생성
     *
     * @param request 요청정보
     * @return 엑세스토큰 정보
     * @throws UsernameNotFoundException 유저정보가 유효하지 않은 경우
     */
    public AuthorizeDto.Response authorizeMember(AuthorizeDto.Request request) {
        Member member = memberRepository.findByMemberName(request.getMemberName())
            .orElseThrow(() -> new UsernameNotFoundException("유효한 사용자 정보가 아닙니다."));

        String generatedToken = jwtTokenGenerator.generateToken(member.getId());

        return AuthorizeDto.Response
            .builder()
            .accessToken(generatedToken)
            .build();
    }
}
