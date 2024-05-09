package com.fx.funxtion.domain.chat.service;

import com.fx.funxtion.domain.chat.dto.*;
import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatMessageRepository;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.dto.ProductCreateResponse;
import com.fx.funxtion.domain.product.dto.ProductDetailResponse;
import com.fx.funxtion.domain.product.dto.ProductUpdateResponse;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private final ChatRoomRepository chatRoomRepository;
    @Autowired
    private final ChatMessageRepository chatMessageRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final ProductRepository productRepository;
    // 채팅 메시지 입력
    public ChatMessageDto insertChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = ChatMessage.builder()
                .userId(chatMessageDto.getUserId())
                .roomId(chatMessageDto.getRoomId())
                .message(chatMessageDto.getMessage())
                .build();
        ChatMessage cm = chatMessageRepository.save(chatMessage);

        return new ChatMessageDto(cm);
    }

    // 채팅 메시지 읽음 처리
    public void updateChatMessage(ChatMessageUpdateRequest chatMessageUpdateRequest) {
        Long roomId = chatMessageUpdateRequest.getRoomId();
        Long userId = chatMessageUpdateRequest.getUserId();
        List<ChatMessage> list = chatMessageRepository.findAllReadMessages(roomId, userId);
        if(!list.isEmpty()) {
            for(int i=0; i<list.size(); i++) {
                list.get(i).setReadYn("Y");
                chatMessageRepository.save(list.get(i));
            }
        }

    }

    // 채팅 내역 조회
    public List<ChatMessageDto> getChatList(long roomId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<ChatMessageDto> list = chatMessageRepository.findAllByRoomId(roomId, sort)
                .stream()
                .map(cm -> new ChatMessageDto(cm))
                .collect(Collectors.toList());
        return list;
    }

    // 채팅방 목록 조회
    public List<ChatRoomDto> getChatRoomList(Long customerId) {
//        Optional<Member> m = memberRepository.findById(customerId);

        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoom(customerId, customerId);
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms) {
            List<ChatMessage> messages = chatMessageRepository.findTopByRoomIdOrderByUpdateDateDesc(chatRoom.getId());
            chatRoomDtos.add(new ChatRoomDto(chatRoom, messages));

//            ChatRoomDto chatRoomDto = new ChatRoomDto();
//            chatRoomDto.setRoomId(chatRoom.getRoomId());
//            chatRoomDto.setProductId(chatRoom.getProductId());
//            chatRoomDto.setStoreId(chatRoom.getStoreId());
//            chatRoomDto.setCustomerId(chatRoom.getCustomerId());
//            chatRoomDto.setUpdateDate(chatRoom.getUpdateDate());
//            chatRoomDto.setCreateDate(chatRoom.getCreateDate());
//
//            List<ChatMessage> messages = chatRoom.getChatMessages();
//            List<ChatMessageDto> messageDtos = new ArrayList<>();
//            for(ChatMessage message : messages) {
//                ChatMessageDto messageDto = new ChatMessageDto();
//                messageDto.setId(message.getId());
//                messageDto.setUserId(message.getUserId());
//                messageDto.setRoomId(message.getRoomId());
//                messageDto.setCreateDate(message.getCreateDate());
//                messageDto.setUpdateDate(message.getUpdateDate());
//                messageDto.setMessage(message.getMessage());
//                messageDto.setReadYn(message.getReadYn());
//            }
//            chatRoomDto.setChatMessages(messages);
//            chatRoomDtos.add(chatRoomDto);
        }
        return chatRoomDtos;
    }

    // 채팅방 생성
    public ChatRoomCreateResponse insertChatRoom(ChatRoomCreateRequest chatRoomCreateRequestDto) {
//        ChatRoomId id = new ChatRoomId(storeId,customerId);
        Member member = memberRepository.findById(chatRoomCreateRequestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상점이 존재하지 않습니다."));

        Product product = productRepository.findById(chatRoomCreateRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .customerId(chatRoomCreateRequestDto.getCustomerId())
                .product(product)
                .createDate(chatRoomCreateRequestDto.getCreateDate())
                .updateDate(chatRoomCreateRequestDto.getUpdateDate())
                .build();
        ChatRoom cr = chatRoomRepository.save(chatRoom);

        return new ChatRoomCreateResponse(cr, new ArrayList<>());
    }

    public RsData<ChatRoomDetailResponse> getChatRoomdetail(Long roomId) {
//        Optional<Product> optionalProduct = productRepository.findById(productId);
        Long id = roomId;
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
//        return optionalProduct.map(product -> RsData.of("200", "상품 조회 성공!", new ProductDetailResponse(product)))
//                .orElseGet(() -> RsData.of("500", "상품 조회 실패!"));
        return optionalChatRoom.map(chatRoom -> RsData.of("200", roomId+"번방 조회 성공", new ChatRoomDetailResponse(chatRoom)))
                .orElseGet(() -> RsData.of("500", "방 조회 실패!"));
    }


}
