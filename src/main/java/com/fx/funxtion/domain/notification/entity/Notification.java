package com.fx.funxtion.domain.notification.entity;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name="notifications")
public class Notification extends BaseEntity {
    private String message;

    @OneToOne
    @JoinColumn(name="receiver_id")
    private Member member;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

}
