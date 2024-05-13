package com.fx.funxtion.domain.member.dto;

import lombok.Getter;

@Getter
public class KakaoLoginRequest {
    private String id;
    private String email;
    private String nickname;
    private String profileImageUrl;
}
