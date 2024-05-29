package com.fx.funxtion.domain.payments.payment.dto;

import com.fx.funxtion.domain.payments.payment.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaymentDto {
    private String impUid; // 거래 고유 번호
    private String email; // 이메일
    private String status; // 결제여부 paid : 1, 그 외 실패
    private Long amount; // 결제금액
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public PaymentDto(PaymentEntity paymentEntity) {
        BeanUtils.copyProperties(paymentEntity, this);
    }
}
