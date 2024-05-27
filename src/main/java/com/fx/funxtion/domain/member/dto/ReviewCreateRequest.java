package com.fx.funxtion.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private Long buyerId;

    private Long sellerId;

    private Long productId;

    private String content;

    private int rating;
}
