package com.fx.funxtion.domain.payments.safepayment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
public class SafePaymentsDetailResponse {


    private String sellerOk;
    private String buyerOk;
    private String status;

}
