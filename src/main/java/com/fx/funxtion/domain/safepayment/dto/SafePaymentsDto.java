package com.fx.funxtion.domain.safepayment.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class SafePaymentsDto {

    private Long id;
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String sellerOk;
    private String buyerOk;
    private String startYn;
    private String endYn;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public SafePaymentsDto(SafePayments safePayments) {
        this.id = safePayments.getId();
        this.productId = safePayments.getProductId();
        this.sellerId = safePayments.getSellerId();
        this.buyerId = safePayments.getBuyerId();
        this.sellerOk = safePayments.getSellerOk();
        this.buyerOk = safePayments.getBuyerOk();
        this.startYn = safePayments.getStartYn();
        this.endYn = safePayments.getEndYn();
        this.createDate = safePayments.getCreateDate();
        this.updateDate = safePayments.getUpdateDate();

    }

}
