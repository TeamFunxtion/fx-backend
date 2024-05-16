package com.fx.funxtion.domain.chat.dto;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.dto.ProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@ToString
@Setter
public class ChatRoomDto {

    private Long id;
    private MemberDto store;
    private MemberDto customer;
    private ProductDto product;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.store = new MemberDto(chatRoom.getMember());
        this.customer = new MemberDto(chatRoom.getCustomer());
        this.product = new ProductDto(chatRoom.getProduct());
        this.createDate = chatRoom.getCreateDate();
        this.updateDate = chatRoom.getUpdateDate();
    }
}
