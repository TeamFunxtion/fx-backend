package com.fx.funxtion.domain.transactionhistory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SellSelectRequest {
    private Long buyerId;
    private Long productId;
}
