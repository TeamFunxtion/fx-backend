package com.fx.funxtion.domain.product.entity;

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
@Table(name="product_images")
public class ProductImage extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String originalName;
    private String imageUrl;
    private Long size;
    private String mainYn;
}
