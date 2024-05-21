package com.fx.funxtion.domain.favorites.dto;
import com.fx.funxtion.domain.favorites.entity.UserFavorites;
import com.fx.funxtion.domain.product.entity.Product;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
public class UserFavoritesDto {

    private Long id;
    private Long userId;
    private Product product;
    private LocalDateTime createDate;

    public UserFavoritesDto(UserFavorites userFavorites) {
        this.id = userFavorites.getId();
        this.userId = userFavorites.getUserId();
        this.product = userFavorites.getProduct();
        this.createDate = userFavorites.getCreateDate();
    }
}
