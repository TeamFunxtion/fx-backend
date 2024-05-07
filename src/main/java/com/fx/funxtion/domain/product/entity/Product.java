package com.fx.funxtion.domain.product.entity;

import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name="products")
public class Product extends BaseEntity {
    private Long storeId;

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
}
