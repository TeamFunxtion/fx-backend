package com.fx.funxtion.domain.favorites.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserFavoritesUpdateRequest {
    private Long userId;
    private Long productId;
}
