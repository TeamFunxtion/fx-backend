package com.fx.funxtion.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class ChatMessageUpdateRequest {
    private Long id;
    private Long userId;
    private Long roomId;
}
