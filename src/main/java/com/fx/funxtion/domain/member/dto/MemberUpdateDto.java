package com.fx.funxtion.domain.member.dto;

import com.fx.funxtion.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateDto{
    private String email;
    private String nickname;
    private String intro;
    private String password;
    private String confirmPassword;
    private String newPassword; // 새로운 비밀번호(newpassword)의 getter 메서드 추가
    private String phoneNumber;

    public MemberUpdateDto(Member member){
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.intro = member.getIntro();
        this.password = member.getPassword();
        this.confirmPassword = member.getPassword();
        this.newPassword = member.getPassword();
        this.phoneNumber = member.getPhoneNumber();
    }

    public CharSequence getNewPassword() {
        return newPassword;
    }
}

