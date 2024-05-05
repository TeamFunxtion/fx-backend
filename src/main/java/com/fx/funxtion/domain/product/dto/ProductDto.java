package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private int storeId;
    private String categoryId;
    private String productTitle;
    private String productDesc;
    private int productPrice;
    private String salesTypeId;
    private String qualityTypeId;
    private String statusTypeId;
    private String location;
    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public ProductDto(Product p) {
        this.id = p.getId();
        this.storeId = p.getStoreId();
        this.categoryId = p.getCategoryId();
        this.productTitle = p.getProductTitle();
        this.productDesc = p.getProductDesc();
        this.productPrice = p.getProductPrice();
        this.salesTypeId = p.getSalesTypeId();
        this.qualityTypeId = p.getQualityTypeId();
        this.statusTypeId = p.getStatusTypeId();
        this.location = p.getLocation();
        this.views = p.getViews();
        this.createDate = p.getCreateDate();
        this.updateDate = p.getUpdateDate();
    }
}
