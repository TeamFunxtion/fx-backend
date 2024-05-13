package com.fx.funxtion.domain.product.dto;

import lombok.Getter;

@Getter
public class FavoriteUpdateRequest {
    private Long userId;
    private Long productId;
}
