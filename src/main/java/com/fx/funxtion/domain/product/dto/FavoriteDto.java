package com.fx.funxtion.domain.product.dto;
import com.fx.funxtion.domain.product.entity.Favorite;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductImage;
import com.fx.funxtion.domain.product.repository.ProductImageRepository;
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
    private LocalDateTime createDate;
    private ProductDto product;


    public FavoriteDto(Favorite favorite) {
        this.id = favorite.getId();
        this.userId = favorite.getUserId();
        this.createDate = favorite.getCreateDate();
        this.product = new ProductDto(favorite.getProduct());

    }
}
