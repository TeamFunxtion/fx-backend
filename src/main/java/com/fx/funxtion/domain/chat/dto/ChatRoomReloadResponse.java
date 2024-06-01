package com.fx.funxtion.domain.chat.dto;

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
public class ChatRoomReloadResponse {

    private ProductDto product;

    public ChatRoomReloadResponse(ChatRoom chatRoom) {

        this.product = new ProductDto(chatRoom.getProduct());

    }

}
