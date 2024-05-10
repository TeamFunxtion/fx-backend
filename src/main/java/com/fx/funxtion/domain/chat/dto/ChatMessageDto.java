package com.fx.funxtion.domain.chat.dto;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long userId;
    private Long roomId;
    private String message;
    private String readYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public ChatMessageDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.userId = chatMessage.getUserId();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();
        this.readYn = chatMessage.getReadYn();
        this.createDate = chatMessage.getCreateDate();
        this.updateDate = chatMessage.getUpdateDate();
    }
}
