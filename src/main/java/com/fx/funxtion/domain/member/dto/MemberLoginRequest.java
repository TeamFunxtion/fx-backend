package com.fx.funxtion.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
