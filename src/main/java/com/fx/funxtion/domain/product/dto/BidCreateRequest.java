package com.fx.funxtion.domain.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidCreateRequest {
    private Long bidderId;
    private Long productId;
    private Long bidPrice;
}
