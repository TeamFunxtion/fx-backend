package com.fx.funxtion.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.rq.Rq;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;

    /**
     * 이메일 로그인
     * @param memberLoginRequest
     * @return RsData<MemberDto>
     */
    @PostMapping("/login")
    public RsData<MemberDto> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest) {
        try {
            // username, password => accessToken
            RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
            if(authAndMakeTokensRs.getResultCode().equals("500") || authAndMakeTokensRs.getData() == null) {
                return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg());
            }
            // 쿠키에 accessToken, refreshToken 넣기
            rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());
            rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());

            return RsData.of(authAndMakeTokensRs.getResultCode(),authAndMakeTokensRs.getMsg(), new MemberDto(authAndMakeTokensRs.getData().getMember()));
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 로그아웃
     * @return RsData<Void>
     */
    @PostMapping("/logout")
    public RsData<Void> logout() {
        rq.logout();
        return RsData.of("200", "로그아웃 성공");
    }

    /**
     * 회원정보 상세 조회
     * @param userId
     * @return RsData<MemberDto>
     */
    @GetMapping("")
    public RsData<MemberDto> getUser(@RequestParam("id") Long userId) {
        try {
            return memberService.getUser(userId);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 회원가입
     * @param memberJoinRequest
     * @return RsData<MemberDto>
     */
    @PostMapping("/join")
    public RsData<MemberDto> join(@Valid @RequestBody MemberJoinRequest memberJoinRequest) {
        try {
            RsData<MemberDto> joinRs = memberService.join(memberJoinRequest);
            return RsData.of(joinRs.getResultCode(), joinRs.getMsg(), joinRs.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 이메일 인증
     * @param email
     * @param code
     * @return RedirectView
     */
    @GetMapping("/auth")
    public RedirectView auth(@RequestParam(value="email") String email, @RequestParam(value="code") String code) {
        RedirectView redirectView = new RedirectView();
        try {
            String verifiedYn = memberService.verifyEmail(email, code);
            redirectView.setUrl("http://localhost:3000/auth/login?auth=success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redirectView;
    }

    /**
     * 포인트 결제 가능 여부 조회
     * @param memberHasMoneyRequest
     * @return RsData<Boolean>
     */
    @PostMapping("/has-money")
    public RsData<Boolean> hasMoney(@RequestBody MemberHasMoneyRequest memberHasMoneyRequest) {
        try {
            boolean hasMoney = memberService.hasMoney(memberHasMoneyRequest);
            return RsData.of("200", "잔액 조회 성공!", hasMoney);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 소셜 로그인(카카오)
     * @param kakaoLoginRequest
     * @return RsData<MemberDto>
     */
    @PostMapping("/kakao/login")
    public RsData<MemberDto> kakaoLogin(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        try {
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
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 회원정보 수정
     * @param multipartFile
     * @param updateUser
     * @return RsData<MemberDto>
     */
    @PutMapping("/update")
    public RsData<MemberDto> putUpdateMember(@RequestParam(value="file", required = false) MultipartFile multipartFile, @RequestParam(value="updateUser", required = true) String updateUser) {
        System.out.println(multipartFile);
        try {
            // 객체는 client에서 직렬화를 하여 전달
            MemberUpdateDto memberUpdateDto = new ObjectMapper().readValue(updateUser, MemberUpdateDto.class); // String to Object
            System.out.println("memberUpdateDto= " + memberUpdateDto);
            RsData<MemberDto> updateMemberRs = memberService.updateMember(multipartFile, memberUpdateDto);
            return RsData.of(updateMemberRs.getResultCode(), updateMemberRs.getMsg(), updateMemberRs.getData());

        } catch(Exception e) {
            return RsData.of("500", "잘못된 회원 정보입력입니다.");
        }
    }

    /**
     * 회원 탈퇴
     * @param memberId
     * @return RsData<Void>
     */
    @DeleteMapping("/delete/{id}")
    public RsData<Void> deleteMember(@PathVariable("id") Long memberId) {
        try {
            return memberService.deleteMember(memberId);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}


