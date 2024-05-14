package com.fx.funxtion.domain.chat.entity;

import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Table(name="chat_messages")
@DynamicInsert
public class ChatMessage extends BaseEntity {
    private Long userId;
    @Column(name = "room_id")
    private Long roomId;
    private String message;
    @ColumnDefault("'N'")
    @Column(length = 1)
    private String readYn;



}
