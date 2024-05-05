package com.fx.funxtion.domain.product.entity;

import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name="products")
public class Product extends BaseEntity {
    private int storeId;

    private String categoryId;

    private String productTitle;

    private String productDesc;

    private int productPrice;

    @Column(name="sales_type_id")
    private String salesTypeId;

    @Column(name="quality_type_id")
    private String qualityTypeId;

    @Column(name="status_type_id")
    private String statusTypeId;

    private String location;

    private int views;
}
