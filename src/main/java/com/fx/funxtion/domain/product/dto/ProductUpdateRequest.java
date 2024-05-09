package com.fx.funxtion.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductUpdateRequest {
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
    private int endDays;
}
