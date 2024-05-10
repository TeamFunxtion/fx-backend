package com.fx.funxtion.domain.chat.repository;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomId(Long roomId, Sort sort);

    List<ChatMessage> findTopByRoomIdOrderByCreateDateDesc(Long roomId);

    @Query(value = "select * from chat_message where room_id = ?1 and user_id != ?2", nativeQuery = true)
    List<ChatMessage> findAllReadMessages(Long roomId, Long userId);
}