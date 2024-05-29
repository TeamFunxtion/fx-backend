package com.fx.funxtion.domain.payments.transactionhistory.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BuySelectRequest {
    private Long buyerId;
    private Long productId;
}
