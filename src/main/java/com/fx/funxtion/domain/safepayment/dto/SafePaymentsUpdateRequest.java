package com.fx.funxtion.domain.safepayment.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
public class SafePaymentsUpdateRequest {
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String status;
}
