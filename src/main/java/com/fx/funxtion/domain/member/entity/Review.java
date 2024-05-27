package com.fx.funxtion.domain.member.entity;

import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Table(name="user_reviews")
public class Review extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;

    private int rating;
}
