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
public class ReviewDto {
    private Long id;

    private Long buyerId;

    private Long sellerId;

    private Long productId;

    private String content;

    private int rating;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public ReviewDto(Review review) {
        BeanUtils.copyProperties(review, this);
    }
}
