package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.dto.MemberHasMoneyRequest;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.jwt.JwtProvider;
import com.fx.funxtion.global.security.SecurityUser;
import com.fx.funxtion.global.util.mail.MailUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailUtils mailUtils;

    enum Roles {
        NOTHING,
        ROLE_USER,
        ROLE_ADMIN
    }


    public Member join(String email, String password) {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roleId(Long.valueOf(Roles.ROLE_USER.ordinal()))
                .point(0)
                .deleteYn("N")
                .verifiedYn("N")
                .build();

        String refreshToken = jwtProvider.genRefreshToken(member);
        member.setRefreshToken(refreshToken);

        // 이메일 인증 메일 발송
        MailUtils.SendMailResponseBody sendMailRs = mailUtils.sendMail(member.getEmail(), "email");
        member.setAuthCode(sendMailRs.getAuthCode());

        memberRepository.save(member);

        return member;
    }

    public boolean validateToken(String token) {
        return jwtProvider.verify(token);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("존재하지 않는 리프레시 토큰입니다."));

        String accessToken = jwtProvider.genAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);

        long id = (int) payloadBody.get("id");
        String email = (String) payloadBody.get("email");
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUser(id, email, "", authorities);
    }

    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokensResponseBody {
        private Member member;
        private String accessToken;
        private String refreshToken;

    }

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String email, String password) {
        Optional<Member> member = this.memberRepository.findByEmailAndDeleteYn(email, "N");
//                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        if(!member.isPresent() || !bCryptPasswordEncoder.matches(password, member.get().getPassword())) {
            return RsData.of("500", "로그인 실패!", null);
        }

        if(member.get().getVerifiedYn().equals("N")) {
            return RsData.of("500", "이메일 인증이 완료되지 않았습니다!", null);
        }

        // Access Token 생성
        String accessToken = jwtProvider.genAccessToken(member.get());
        // Refresh Token 생성
        String refreshToken = jwtProvider.genRefreshToken(member.get());

        System.out.println("accessToken : " + accessToken);

        return RsData.of("200", "로그인 성공!", new AuthAndMakeTokensResponseBody(member.get(), accessToken, refreshToken));
    }

    @Transactional
    public String verifyEmail(String email, String authCode) {
        Member member = memberRepository.findByEmail(email).get();

        if(member.getAuthCode().equals(authCode)) {
            member.setVerifiedYn("Y");
        }
        memberRepository.save(member);

        return member.getVerifiedYn();
    }

    public boolean hasMoney(MemberHasMoneyRequest memberHasMoneyRequest) {
        Member member = memberRepository.findById(memberHasMoneyRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return (member.getPoint() - memberHasMoneyRequest.getPoint()) >= 0;
    }
}
