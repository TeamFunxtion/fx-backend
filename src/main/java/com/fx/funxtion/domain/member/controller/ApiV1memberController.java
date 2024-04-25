package com.fx.funxtion.domain.member.controller;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1memberController {
    private final MemberService memberService;

    @Getter
    public static class LoginRequestBody {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponseBody {
        private MemberDto memberDto;
    }

    @PostMapping("/login")
    public RsData<LoginResponseBody> login (@Valid @RequestBody LoginRequestBody loginRequestBody, HttpServletResponse response) {

        // username, password => accessToken
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(loginRequestBody.getUsername(), loginRequestBody.getPassword());

        ResponseCookie cookie = ResponseCookie.from("accessToken", authAndMakeTokensRs.getData().getAccessToken())
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new LoginResponseBody(new MemberDto(authAndMakeTokensRs.getData().getMember())));
    }

    @GetMapping("/me")
    public String me() {
        return "accessToken 있어서 내 정보 get OK!";
    }
}
