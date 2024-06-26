package com.fx.funxtion.domain.member.dto;

import com.fx.funxtion.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberDto {
    private long id;
    private String email;
    private Long roleId;
    private String profileImageUrl;
    private String nickname;
    private String intro;
    private int point;
    private String phoneNumber;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int reviewCount;
    private double reviewScore;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.roleId = member.getRoleId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.intro = member.getIntro();
        this.point = member.getPoint();
        this.phoneNumber = member.getPhoneNumber();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }

    public MemberDto(Member member, int reviewCount, double reviewScore) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.roleId = member.getRoleId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.intro = member.getIntro();
        this.point = member.getPoint();
        this.phoneNumber = member.getPhoneNumber();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
    }


}
