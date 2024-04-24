package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        memberRepository.save(member);

        return member;
    }

    @AllArgsConstructor
    @Getter
    public static class AuthAndMakeTokensResponseBody {
        private Member member;
        private String accessToken;

    }

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String email, String password) {
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        // 시간 설정 및 토큰 생성
        String accessToken = jwtProvider.genToken(member, 60 * 60 * 5);

        System.out.println("accessToken : " + accessToken);

        return RsData.of("200-1", "로그인 성공", new AuthAndMakeTokensResponseBody(member, accessToken));
    }
}
