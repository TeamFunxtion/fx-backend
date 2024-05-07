package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProductUpdateResponse {
    private Long id;
    private Long storeId;
    private String categoryId;
    private String productTitle;
    private String productDesc;
    private Long productPrice;
    private String salesTypeId;
    private String qualityTypeId;
    private String statusTypeId;
    private String location;
    private Long coolPrice;
    private Long currentPrice;
    private LocalDateTime endTime;
    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public ProductUpdateResponse(Product p) {
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
        this.coolPrice = p.getCoolPrice();
        this.currentPrice = p.getCurrentPrice();
        this.endTime = p.getEndTime();
        this.views = p.getViews();
        this.createDate = p.getCreateDate();
        this.updateDate = p.getUpdateDate();
    }
}