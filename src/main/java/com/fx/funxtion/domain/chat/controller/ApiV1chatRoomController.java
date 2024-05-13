package com.fx.funxtion.domain.chat.controller;

import com.fx.funxtion.domain.chat.dto.*;
import com.fx.funxtion.domain.chat.service.ChatService;
import com.fx.funxtion.domain.product.dto.ProductDetailResponse;
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


    // 전체 채팅방 목록 조회
    @GetMapping("")
    public RsData<List> findAllRoom(@RequestParam("id") String id) {
        Long customerId = Long.parseLong(id);
        List<ChatRoomListResponse> list = chatService.getChatRoomList(customerId);
        return RsData.of("200", "채팅방 조회 성공!", list);
    }

    // 특정 채팅방 조회
    @GetMapping("/{id}")
    public RsData<ChatRoomDetailResponse> findRoom(@RequestParam("id") String id) {
        Long roomId = Long.parseLong(id);
        RsData<ChatRoomDetailResponse> chatRoomDetailResponse = chatService.getChatRoomdetail(roomId);
        return RsData.of(chatRoomDetailResponse.getResultCode(), chatRoomDetailResponse.getMsg(), chatRoomDetailResponse.getData());
    }

    // 채팅방 추가
    @PostMapping("")
    public RsData<ChatRoomCreateResponse> enter(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {

        ChatRoomCreateResponse enterRoom = chatService.insertChatRoom(chatRoomCreateRequest);

        return RsData.of("200", "채팅방 추가 성공!", enterRoom);
    }

    // 채팅 내역 조회
    @GetMapping("/{id}/messages")   // pathVariable
    public RsData<List> findMsg(@RequestParam("id") String id){
        Long roomNo = Long.parseLong(id);
        List<ChatMessageDto> list = chatService.getChatList(roomNo);

        return RsData.of("200", "채팅내용 조회 성공!", list);
    }

    // 채팅 메시지 추가(입력)
    @PostMapping("/{id}/messages")
    public RsData<ChatMessageDto> insert(@RequestBody ChatMessageDto chatMessageDto) {
        ChatMessageDto insertDto = chatService.insertChatMessage(chatMessageDto);

        return RsData.of("200", "채팅 입력 성공!", insertDto);
    }

    // 채팅 읽음 처리
    @PatchMapping("/{id}/messages")
    public void updateMsg(@RequestBody ChatMessageUpdateRequest chatMessageUpdateRequest) {
        chatService.updateChatMessage(chatMessageUpdateRequest);

    }

}
