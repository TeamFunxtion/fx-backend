package com.fx.funxtion.domain.chat.controller;

import com.fx.funxtion.domain.chat.dto.*;
import com.fx.funxtion.domain.chat.service.ChatService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
@Slf4j
public class ApiV1chatRoomController {

    private final ChatService chatService;

    /**
     * 전체 채팅방 목록 조회
     * @param id
     * @return RsData<List>
     */
    @GetMapping("")
    public RsData<List> findAllRoom(@RequestParam("id") String id) {
        Long customerId = Long.parseLong(id);
        try {
            List<ChatRoomWithMessagesDto> list = chatService.getChatRoomList(customerId);
            return RsData.of("200", "채팅방 조회 성공!", list);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 특정 채팅방 조회
     * @param id
     * @return RsData<ChatRoomDetailResponse>
     */
    @GetMapping("/{id}")
    public RsData<ChatRoomDetailResponse> findRoom(@RequestParam("id") String id) {
        Long roomId = Long.parseLong(id);
        try {
            RsData<ChatRoomDetailResponse> chatRoomDetailResponse = chatService.getChatRoomdetail(roomId);
            return RsData.of(chatRoomDetailResponse.getResultCode(), chatRoomDetailResponse.getMsg(), chatRoomDetailResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 채팅방 생성,변경
     * @param chatRoomCreateRequest
     * @return RsData<Long>
     */
    @PostMapping("")
    public RsData<Long> enter(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
        try {
            Long id = chatService.insertChatRoom(chatRoomCreateRequest);
            return RsData.of("200", "채팅방 추가 성공", id);
        } catch(Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 채팅 내역 조회
     * @param id
     * @return RsData<List>
     */
    @GetMapping("/{id}/messages")   // pathVariable
    public RsData<List> findMsg(@RequestParam("id") String id){
        Long roomNo = Long.parseLong(id);
        try {
            List<ChatMessageDto> list = chatService.getChatList(roomNo);
            return RsData.of("200", "채팅내용 조회 성공!", list);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 채팅 읽음 처리
     * @param chatMessageUpdateRequest
     * @return RsData<Void>
     */
    @PatchMapping("/{id}/messages")
    public RsData<Void> updateMsg(@RequestBody ChatMessageUpdateRequest chatMessageUpdateRequest) {
        try {
            chatService.updateChatMessage(chatMessageUpdateRequest);
            return RsData.of("200", "읽음 처리 성공!");
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
