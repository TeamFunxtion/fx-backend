package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

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
        BeanUtils.copyProperties(p, this);
    }
}