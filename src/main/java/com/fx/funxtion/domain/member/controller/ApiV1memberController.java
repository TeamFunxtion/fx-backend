package com.fx.funxtion.domain.member.controller;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.rq.Rq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1memberController {
    private final MemberService memberService;
    private final Rq rq;


    @Getter
    public static class LoginRequestBody {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponseBody {
        private MemberDto memberDto;
    }

    @PostMapping("/login")
    public RsData<LoginResponseBody> login (@Valid @RequestBody LoginRequestBody loginRequestBody) {

        // username, password => accessToken
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(loginRequestBody.getEmail(), loginRequestBody.getPassword());

        if(authAndMakeTokensRs.getResultCode().equals("500") || authAndMakeTokensRs.getData() == null) {
            return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new LoginResponseBody(null));
        }

        // 쿠키에 accessToken, refreshToken 넣기
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());
        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());

        return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new LoginResponseBody(new MemberDto(authAndMakeTokensRs.getData().getMember())));
    }

    @PostMapping("/logout")
    public RsData<Void> logout() {
        rq.logout();
        return RsData.of("200", "로그아웃 성공");
    }

    @Getter
    @AllArgsConstructor
    private static class MeResponseBody {
        private final MemberDto memberDto;
    }

    @GetMapping("/me")
    public RsData<MeResponseBody> me() {
        Member member = rq.getMember();
        return RsData.of(
                "200",
                "내 정보 조회 성공",
                new MeResponseBody(new MemberDto(member)));
    }

    @Getter
    public static class JoinRequestBody {
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String passwordConfirm;
    }

    @PostMapping("/join")
    public RsData<Void> join(@Valid @RequestBody JoinRequestBody joinRequestBody) {
        System.out.println(joinRequestBody);

        if(!joinRequestBody.getPassword().equals(joinRequestBody.getPasswordConfirm())) {
            return RsData.of("500", "회원가입 실패..");
        }

        Member member = memberService.join(joinRequestBody.getEmail(), joinRequestBody.getPassword());
        return RsData.of("200", "회원가입 성공!");
    }
}
