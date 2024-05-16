package com.fx.funxtion.domain.safepayment.entity;

import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Table(name="safe_payments")
@DynamicInsert
public class SafePayments extends BaseEntity {
    private Long productId;
    private Long sellerId;
    private Long buyerId;
    private String sellerOk;
    private String buyerOk;
    private String startYn;
    private String endYn;
}