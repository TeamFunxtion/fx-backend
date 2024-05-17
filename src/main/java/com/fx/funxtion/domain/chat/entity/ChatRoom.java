package com.fx.funxtion.domain.chat.entity;


import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Table(name="chat_rooms" , uniqueConstraints = {
        @UniqueConstraint(
                columnNames={"store_id", "customer_id"}
        )
})
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom extends BaseEntity{

    @OneToOne
    @JoinColumn(name="customer_id")
    private Member customer;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name="store_id")
    private Member member;



}
