package com.fx.funxtion.domain.product.dto;
import com.fx.funxtion.domain.product.entity.Favorite;
import com.fx.funxtion.domain.product.entity.Product;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
public class FavoriteDto {

    private Long id;
    private Long userId;
    private Product product;
    private LocalDateTime createDate;

    public FavoriteDto(Favorite favorite) {
        this.id = favorite.getId();
        this.userId = favorite.getUserId();
        this.product = favorite.getProduct();
        this.createDate = favorite.getCreateDate();
    }
}
