package com.fx.funxtion.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.product.dto.BidDto;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
//@ToString(callSuper = true)
@Table(name="products")
public class Product extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Member member;

    private String categoryId;

    private String productTitle;

    private String productDesc;

    private Long productPrice;

    @Column(name="sales_type_id")
    private String salesTypeId;

    @Column(name="quality_type_id")
    private String qualityTypeId;

    @Column(name="status_type_id")
    private String statusTypeId;

    private String location;

    private Long auctionWinnerId;

    private Long currentPrice;

    private Long coolPrice;

    private LocalDateTime endTime;

    private int views;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties({"product"})
    @OrderBy("id desc")
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties({"product"})
    private List<Bid> bids = new ArrayList<>();
}
