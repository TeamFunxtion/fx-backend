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
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.createDate = member.getCreateDate();
        this.updateDate = member.getUpdateDate();
    }
}
