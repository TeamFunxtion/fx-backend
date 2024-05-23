package com.fx.funxtion.domain.chat.service;

import com.fx.funxtion.domain.chat.dto.*;
import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatMessageRepository;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.domain.safepayment.entity.SafePaymentStatus;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.safepayment.repository.SafePaymentsRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final MemberRepository memberRepository;

    private final ProductRepository productRepository;

    private final SafePaymentsRepository safePaymentsRepository;

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
    public List<ChatRoomWithMessagesDto> getChatRoomList(Long customerId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllChatRoom(customerId, customerId);
        List<ChatRoomWithMessagesDto> chatRoomListResponse = new ArrayList<>();

        for(ChatRoom chatRoom : chatRooms) {
            List<ChatMessage> messages = chatMessageRepository.findTopByRoomIdOrderByCreateDateDesc(chatRoom.getId());

            chatRoomListResponse.add(new ChatRoomWithMessagesDto(chatRoom, messages));
        }
        return chatRoomListResponse;
    }



    // 채팅방 생성
    public Long insertChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {
        Member member = memberRepository.findById(chatRoomCreateRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상점이 존재하지 않습니다."));
        Member customer = memberRepository.findById(chatRoomCreateRequest.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 존재하지 않습니다."));
        Product product = productRepository.findById(chatRoomCreateRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        ChatRoom chatRoomEx = chatRoomRepository
                            .findByCustomerIdAndMemberId(chatRoomCreateRequest.getCustomerId(), chatRoomCreateRequest.getStoreId());
        ChatRoom cr;
        if(chatRoomEx != null) {
            List<SafePayments> safePaymentsList = safePaymentsRepository
                            .findBySellerIdAndBuyerId(chatRoomCreateRequest.getStoreId(), chatRoomCreateRequest.getCustomerId());
            if(safePaymentsList == null) {
                chatRoomEx.setProduct(product);
                cr = chatRoomRepository.save(chatRoomEx);
                return cr.getId();
            }
            boolean hasSP03 = false;
            for (int i = 0; i < safePaymentsList.size(); i++) {
                if (safePaymentsList.get(i).getStatus().equals(SafePaymentStatus.SP03)) {
                    hasSP03 = true;
                    break;
                }
            }
            if (hasSP03) {
                return chatRoomEx.getId();
            } else {
                chatRoomEx.setProduct(product);
                cr = chatRoomRepository.save(chatRoomEx);
                return cr.getId();  // SP03 상태를 가진 항목이 하나도 없으면 이 값을 반환합니다.
            }
        } else {
            ChatRoom chatRoom = ChatRoom.builder()
                    .member(member)
                    .customer(customer)
                    .product(product)
                    .build();
            cr = chatRoomRepository.save(chatRoom);
            return cr.getId();
        }

    }


    // 채팅방 상세정보 및 채팅메시지 조회
    public RsData<ChatRoomDetailResponse> getChatRoomdetail(Long roomId) {
        Long id = roomId;
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(id);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<ChatMessageDto> list = chatMessageRepository.findAllByRoomId(roomId, sort)
                .stream()
                .map(cm -> new ChatMessageDto(cm))
                .collect(Collectors.toList());

        return optionalChatRoom.map(chatRoom -> RsData.of("200", roomId+"번방 조회 성공", new ChatRoomDetailResponse(chatRoom, list)))
                .orElseGet(() -> RsData.of("500", "방 조회 실패!"));
    }


}
