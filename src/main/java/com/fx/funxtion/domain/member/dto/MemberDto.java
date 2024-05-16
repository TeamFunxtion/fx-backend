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
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.roleId = member.getRoleId();
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.intro = member.getIntro();
        this.point = member.getPoint();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }


}
