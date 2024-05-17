package com.fx.funxtion.domain.follow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
public class FollowUpdateRequest {

    private Long toId;
    private Long fromId;
}
