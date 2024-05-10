package com.fx.funxtion;

import com.fx.funxtion.domain.chat.dto.*;
import com.fx.funxtion.domain.chat.repository.ChatMessageRepository;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
import com.fx.funxtion.domain.chat.service.ChatService;
import com.fx.funxtion.global.RsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ChatTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // chatRoom 추가
    @Test
    @DisplayName("채팅방 추가")
    public void enter() {
        ChatRoomCreateRequest chatRoomCreateRequest = new ChatRoomCreateRequest();
        chatRoomCreateRequest.setCustomerId(1L);
        chatRoomCreateRequest.setStoreId(6L);
        chatRoomCreateRequest.setProductId(5L);

        ChatRoomCreateResponse crd = chatService.insertChatRoom(chatRoomCreateRequest);
        System.out.println(crd);
        assertThat(chatRoomCreateRequest.getStoreId()).isEqualTo(crd.getStore().getId());
    }

    // chatRoom 조회
    @Test
    @DisplayName("채팅방 목록 조회")
    public void getList() {
        long customerId = 1;
        List<ChatRoomDto> chatRoomList = chatService.getChatRoomList(customerId);

        for(ChatRoomDto chatRoomDto: chatRoomList) {

            System.out.println(chatRoomDto.getStore().getNickname());
        }
    }

    // chatRoom 한 개 조회
    @Test
    @DisplayName("채팅룸 한 개 조회")
    public void getOne() {
        long id = 2;
        RsData<ChatRoomDetailResponse> chatRoomDetailResponse = chatService.getChatRoomdetail(id);
        System.out.println(chatRoomDetailResponse.getData());
    }

    // chatMessage 추가
    @Test
    @DisplayName("채팅 메시지 추가")
    public void insert() {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setUserId(1);
        chatMessageDto.setRoomId(5);
        chatMessageDto.setMessage("안녕하세요 코딩못하는 버러지입니다.");

        ChatMessageDto cmd = chatService.insertChatMessage(chatMessageDto);

        assertThat(chatMessageDto.getMessage()).isEqualTo(cmd.getMessage());
        System.out.println(cmd.getMessage());
    }

    // chatMessage 조회
    @Test
    @DisplayName("채팅 메시지 조회")
    public void getMessageList() {
        Long roomId = 1L;
        List<ChatMessageDto> chatMessageDtos = chatService.getChatList(roomId);
        for(ChatMessageDto chatMessageDto: chatMessageDtos) {
            System.out.println(chatMessageDto.getMessage());
        }
    }

}
