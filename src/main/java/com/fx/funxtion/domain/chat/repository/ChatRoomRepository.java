package com.fx.funxtion.domain.chat.repository;

import com.fx.funxtion.domain.chat.dto.ChatRoomDto;
import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>  {

    @Query(value = "select * from chat_rooms where store_id = ?1 or customer_id = ?2", nativeQuery = true)
    List<ChatRoom> findAllChatRoom(Long storeId, Long customerId);
    ChatRoom findByCustomerIdAndMemberId(Long customerId, Long memberId);

}
