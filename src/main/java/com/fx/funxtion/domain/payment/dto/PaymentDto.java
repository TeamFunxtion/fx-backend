package com.fx.funxtion.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaymentDto {
    private String impUid; // 거래 고유 번호
    private String status; // 결제여부 paid : 1, 그 외 실패
    private Long amount; // 결제금액
}
