package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.entity.Bid;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private MemberDto creator;
    private String categoryId;
    private String productTitle;
    private String productDesc;
    private Long productPrice;
    private String salesTypeId;
    private String qualityTypeId;
    private String statusTypeId;
    private String location;
    private Long currentPrice;
    private Long coolPrice;
    private LocalDateTime endTime;
    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String thumbnailUrl;
    private int bidCount;

    public ProductDto(Product p) {
        BeanUtils.copyProperties(p, this);
        this.creator = new MemberDto(p.getMember());
        this.bidCount = p.getBids().size();
        if(p.getImages().size() > 0) {
            this.thumbnailUrl = findProductThumbnail(p.getImages()).getImageUrl();
        }
    }

    public ProductDto(Bid b) {
        BeanUtils.copyProperties(b.getProduct(), this);
        this.creator = new MemberDto(b.getMember());
        if(b.getProduct().getImages().size() > 0) {
            this.thumbnailUrl = findProductThumbnail(b.getProduct().getImages()).getImageUrl();
        }
    }

    private ProductImage findProductThumbnail(List<ProductImage> images) {
        Comparator<ProductImage> comparatorById = Comparator.comparingLong(ProductImage::getId);
        ProductImage productImageWithMinId = images.stream()
                .min(comparatorById)
                .orElseThrow(NoSuchElementException::new);
        return productImageWithMinId;
    }
}
