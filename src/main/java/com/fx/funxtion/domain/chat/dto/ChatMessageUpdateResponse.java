package com.fx.funxtion.domain.chat.dto;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class ChatMessageUpdateResponse {
    private Long id;
    private Long userId;
    private Long roomId;
    private String message;
    private String readYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public ChatMessageUpdateResponse(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.userId = chatMessage.getUserId();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();
        this.readYn = chatMessage.getReadYn();
        this.createDate = chatMessage.getCreateDate();
        this.updateDate = chatMessage.getUpdateDate();
    }
}
