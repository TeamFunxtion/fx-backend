package com.fx.funxtion.domain.member.dto;

import com.fx.funxtion.domain.member.entity.Review;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReviewDetailDto {
    private Long id;

    private Long buyerId;
    private String buyerName;

    private Long sellerId;

    private Long productId;
    private String productTitle;
    private String productThumbnail;

    private String content;

    private int rating;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public ReviewDetailDto(Review review) {
        BeanUtils.copyProperties(review, this);
        this.buyerId = review.getBuyer().getId();
        this.buyerName = review.getBuyer().getNickname();
        this.sellerId = review.getProduct().getMember().getId();
        this.productId = review.getProduct().getId();
        this.productTitle = review.getProduct().getProductTitle();
        this.productThumbnail = review.getProduct().getImages().get(0).getImageUrl();
    }
}
