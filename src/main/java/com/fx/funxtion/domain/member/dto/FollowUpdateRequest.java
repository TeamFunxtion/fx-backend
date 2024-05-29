package com.fx.funxtion.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowUpdateRequest {

    private Long toId;
    private Long fromId;
}
