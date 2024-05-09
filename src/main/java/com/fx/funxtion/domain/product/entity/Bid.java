package com.fx.funxtion.domain.product.entity;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bids")
public class Bid extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name="bidder_id")
    private Member member;

    private Long bidPrice;

    private String returnYn;
}
