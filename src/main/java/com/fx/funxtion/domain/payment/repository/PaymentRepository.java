package com.fx.funxtion.domain.payment.repository;

import com.fx.funxtion.domain.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    long countByImpUidContainsIgnoreCase(String impUid); // 결제 고유 번호 중복 확인
}
