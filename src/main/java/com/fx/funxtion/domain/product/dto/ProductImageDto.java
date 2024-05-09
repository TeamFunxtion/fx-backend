package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.product.entity.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductImageDto {
    private Long id;
//    private Long product_id;
//    private String originalName;
    private String imageUrl;
//    private Long size;
    private String mainYn;
//    private LocalDateTime createDate;
//    private LocalDateTime updateDate;

    public ProductImageDto(ProductImage productImage) {
        BeanUtils.copyProperties(productImage, this);
    }
}
