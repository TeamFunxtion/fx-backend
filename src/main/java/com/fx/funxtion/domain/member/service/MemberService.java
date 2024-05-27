package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.jwt.JwtProvider;
import com.fx.funxtion.global.security.SecurityUser;
import com.fx.funxtion.global.util.mail.MailUtils;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ProductRepository productRepository;

    private final String USER_IMAGE_DEFAULT = "https://funxtion-image.s3.amazonaws.com/funxtion/user_default.png";

    enum Roles {
        NOTHING,
        ROLE_USER,
        ROLE_ADMIN
    }
    public RsData<MemberDto> getUser(Long userId) {
        Optional<Member> findMember = memberRepository.findByIdAndDeleteYn(userId, "N");

        return findMember.map(member -> RsData.of("200", "조회 성공!", new MemberDto(member)))
                .orElseGet(() -> RsData.of("500", "조회 실패!"));


    }

    public RsData<MemberDto> join(MemberJoinRequest memberJoinRequest) {

        if(memberRepository.findByEmail(memberJoinRequest.getEmail()).isPresent()) {
            return RsData.of("500", "이미 사용중인 이메일 입니다!");
        }

        String nickname = memberJoinRequest.getEmail().substring(0, memberJoinRequest.getEmail().indexOf("@"));

        Member member = Member.builder()
                .email(memberJoinRequest.getEmail())
                .password(passwordEncoder.encode(memberJoinRequest.getPassword()))
                .profileImageUrl(USER_IMAGE_DEFAULT)
                .nickname(nickname)
                .name(nickname)
                .intro(nickname + "의 소개글입니다.")
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

        return RsData.of("200", "환영합니다! 이메일 인증을 완료해주세요.", new MemberDto(member));
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

    public RsData<AuthAndMakeTokensResponseBody> kakaoLogin(KakaoLoginRequest kakaoLoginRequest) {
        Optional<Member> findMember = memberRepository.findByEmail(kakaoLoginRequest.getEmail());

        if(findMember.isEmpty()) {
            // 회원가입
            String profileImageUrl = !kakaoLoginRequest.getProfileImageUrl().isEmpty() ? kakaoLoginRequest.getProfileImageUrl() : USER_IMAGE_DEFAULT;

            Member newMember = Member.builder()
                    .email(kakaoLoginRequest.getEmail())
                    .password(passwordEncoder.encode("passwordTemp#!$#$13"))
                    .profileImageUrl(profileImageUrl)
                    .nickname(kakaoLoginRequest.getNickname())
                    .name(kakaoLoginRequest.getNickname())
                    .intro(kakaoLoginRequest.getNickname() + "의 소개글입니다.")
                    .socialId(kakaoLoginRequest.getId())
                    .socialProvider("kakao")
                    .roleId(Long.valueOf(Roles.ROLE_USER.ordinal()))
                    .point(0)
                    .deleteYn("N")
                    .verifiedYn("Y")
                    .build();

            String refreshToken = jwtProvider.genRefreshToken(newMember);
            newMember.setRefreshToken(refreshToken);

            memberRepository.save((newMember));

            // Access Token 생성
            String accessToken = jwtProvider.genAccessToken(newMember);
            return RsData.of("200", "회원가입 후 로그인 성공!", new AuthAndMakeTokensResponseBody(newMember, accessToken, refreshToken));
        } else {
            String accessToken = jwtProvider.genAccessToken(findMember.get());
            String refreshToken = jwtProvider.genRefreshToken(findMember.get());

            Member loginMember = findMember.get();
            loginMember.setRefreshToken(refreshToken);
            memberRepository.save(loginMember);

            return RsData.of("200", "로그인 성공!", new AuthAndMakeTokensResponseBody(findMember.get(), accessToken, refreshToken));
        }
    }
    @Transactional
    public RsData<Void> updateMember(@RequestBody MemberUpdateDto memberUpdateDto) {
        try {
            Member findMember = memberRepository.findByEmail(memberUpdateDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 회원이 존재하지 않습니다."));

            if (!passwordEncoder.matches(memberUpdateDto.getPassword(), findMember.getPassword())) {
                return RsData.of("400", "현재 비밀번호가 일치하지 않습니다.");
            }

            if (!memberUpdateDto.getNewPassword().isEmpty()) {
                if (memberUpdateDto.getPassword().equals(memberUpdateDto.getNewPassword())) {
                    return RsData.of("400", "현재 비밀번호와 신규 비밀번호는 같을 수 없습니다");
                }

                if (!memberUpdateDto.getNewPassword().equals(memberUpdateDto.getConfirmNewPassword())) {
                    return RsData.of("400", "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
                }

                String newPasswordEncoded = passwordEncoder.encode(memberUpdateDto.getNewPassword());
                findMember.setPassword(newPasswordEncoded);
            }

            findMember.setNickname(memberUpdateDto.getNickname());
            findMember.setIntro(memberUpdateDto.getIntro());
            findMember.setPhoneNumber(memberUpdateDto.getPhoneNumber());

            memberRepository.save(findMember);

            return RsData.of("200", "회원 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return RsData.of("500", "회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<RsData<Void>> deleteMember(Long memberId) {
        try {
            Optional<Member> optionalMember = memberRepository.findById(memberId);
            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                member.setDeleteYn("Y"); // 삭제 여부를 'Y'로 설정
                memberRepository.save(member);
                return ResponseEntity.ok(RsData.of("200", "회원 탈퇴가 성공적으로 처리되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RsData.of("404", "회원을 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RsData.of("500", "회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}

