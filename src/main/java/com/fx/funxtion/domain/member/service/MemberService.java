package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.jwt.JwtProvider;
import com.fx.funxtion.global.security.SecurityUser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Member join(String email, String password) {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        String refreshToken = jwtProvider.genRefreshToken(member);
        member.setRefreshToken(refreshToken);

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
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // Access Token 생성
        String accessToken = jwtProvider.genAccessToken(member);
        // Refresh Token 생성
        String refreshToken = jwtProvider.genRefreshToken(member);

        System.out.println("accessToken : " + accessToken);

        return RsData.of("200-1", "로그인 성공", new AuthAndMakeTokensResponseBody(member, accessToken, refreshToken));
    }
}
