package kr.co.engagement.app.member.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.engagement.app.member.application.MemberService;
import kr.co.engagement.app.member.application.dto.AuthorizeDto;
import kr.co.engagement.core.model.dto.ResponseModel;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/authorize")
    public ResponseModel<AuthorizeDto.Response> authorize(@RequestBody AuthorizeDto.Request authRequest) {
        AuthorizeDto.Response response = memberService.authorizeMember(authRequest);

        return ResponseModel.ok(response);
    }
}
