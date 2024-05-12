package com.fx.funxtion.global.websocket.controller;

import com.fx.funxtion.domain.chat.dto.ChatMessageDto;
import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @Autowired
    private ChatService chatService
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지 수신 및 저장
    @MessageMapping("/message")
    @Operation(summary = "메시지 전송", description = "메시지를 전송합니다.")
    public ResponseEntity<String> receiveMessage(@RequestBody ChatMessageDto chatMessageDto) {
        //메시지 저장
        ChatMessageDto chatMessage = chatService.insertChatMessage(chatMessageDto);

        // 메시지를 해당 채팅방 구독자들에게 전송
        // convertAndSend 메소드를 통해 /sub/chatroom/{chatRoomId}를 구독하고 있는 사용자들에게 메시지를 보내줌.
        // /pub로 발행된 메시지가 서버로 도착해 저장되고 converAndSend를 통해 구독중인 사용자들에게 메시지를 보냄.
        messagingTemplate.convertAndSend("/sub/chatroom/" + chatMessageDto.getRoomId(), chatMessageDto);
        return ResponseEntity.ok("메시지 전송 완료");
    }
}
