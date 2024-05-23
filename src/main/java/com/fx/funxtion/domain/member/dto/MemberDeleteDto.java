package com.fx.funxtion.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDeleteDto {
    private String email;
    private String password;

    public MemberDeleteDto(String email, String password) {
         this.email = email;
         this.password = password;
    }
}