package com.fx.funxtion.domain.product.dto;

import lombok.Getter;

@Getter
public class ProductReportRequest {
    private Long userId;
    private Long productId;
    private String reportTypeCode;
}
