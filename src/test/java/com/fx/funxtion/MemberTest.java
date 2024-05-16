package com.fx.funxtion;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.member.dto.MemberHasMoneyRequest;
import com.fx.funxtion.domain.member.dto.MemberJoinRequest;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 유저정보_조회() {
        Long userId = 1L;

        RsData<MemberDto> getRs = memberService.getUser(userId);

        Assertions.assertThat(getRs.getResultCode()).isEqualTo("200");
    }

    @Test
    public void join() {
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest();
        memberJoinRequest.setEmail("admin@gmail.com");
        memberJoinRequest.setPassword("1234");

        RsData<MemberDto> member = memberService.join(memberJoinRequest);

        Assertions.assertThat(member.getData().getId()).isGreaterThan(0);
    }

    @Test
    public void login() {
        String email = "sksmsdirjsdnl@gmail.com";
        String password = "1234";

        RsData<MemberService.AuthAndMakeTokensResponseBody> loginRs = memberService.authAndMakeTokens(email, password);

        System.out.println(loginRs.getMsg());
    }

    @Test
    public void verifyEmail() {
        String email = "sksmsdirjsdnl@gmail.com";

        Member member = memberRepository.findByEmail(email).get();
        String authCode = member.getAuthCode();

        String verifiedYn = memberService.verifyEmail(email, authCode);

        System.out.println(verifiedYn);
    }

    @Test
    public void 포인트_사용_여부() {
        MemberHasMoneyRequest memberHasMoneyRequest = new MemberHasMoneyRequest();
        memberHasMoneyRequest.setUserId(1L);
        memberHasMoneyRequest.setPoint(10000L);

        boolean hasMoney = memberService.hasMoney(memberHasMoneyRequest);
        System.out.println(hasMoney);

        Assertions.assertThat(hasMoney).isTrue();
    }
}
