package com.fx.funxtion.domain.member.controller;

import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.rq.Rq;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PostMapping("/login")
    public RsData<MemberDto> login (@Valid @RequestBody MemberLoginRequest memberLoginRequest) {

        // username, password => accessToken
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());

        if(authAndMakeTokensRs.getResultCode().equals("500") || authAndMakeTokensRs.getData() == null) {
            return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg());
        }

        // 쿠키에 accessToken, refreshToken 넣기
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());
        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());

        return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new MemberDto(authAndMakeTokensRs.getData().getMember()));
    }

    @PostMapping("/logout")
    public RsData<Void> logout() {
        rq.logout();
        return RsData.of("200", "로그아웃 성공");
    }

    @PostMapping("/join")
    public RsData<MemberDto> join(@Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        System.out.println(memberJoinRequest);

        if(!memberJoinRequest.getPassword().equals(memberJoinRequest.getPasswordConfirm())) {
            return RsData.of("500", "회원가입 실패..");
        }

        RsData<MemberDto> joinRs = memberService.join(memberJoinRequest);

        return RsData.of(joinRs.getResultCode(), joinRs.getMsg(), joinRs.getData());
    }

    @GetMapping("/auth")
    public String auth(@RequestParam(value="email") String email, @RequestParam(value="code") String code) {
        System.out.println(email);
        System.out.println(code);

        String verifiedYn = memberService.verifyEmail(email, code);
        System.out.println(verifiedYn);

        return "이메일 인증이 완료되었습니다.";
    }

    @PostMapping("/has-money")
    public RsData<Boolean> hasMoney(@RequestBody MemberHasMoneyRequest memberHasMoneyRequest) {

        boolean hasMoney = memberService.hasMoney(memberHasMoneyRequest);

        return RsData.of("200", "잔액 조회 성공!", hasMoney);
    }


    @PostMapping("/kakao/login")
    public RsData<MemberDto> kakaoLogin(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        System.out.println(kakaoLoginRequest);

        memberService.kakaoLogin(kakaoLoginRequest);

        // username, password => accessToken
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.kakaoLogin(kakaoLoginRequest);

        if(authAndMakeTokensRs.getResultCode().equals("500") || authAndMakeTokensRs.getData() == null) {
            return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg());
        }

        // 쿠키에 accessToken, refreshToken 넣기
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());
        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());

        return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new MemberDto(authAndMakeTokensRs.getData().getMember()));
    }
}


