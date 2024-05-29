package com.fx.funxtion.domain.payments.payment.repository;

import com.fx.funxtion.domain.payments.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    long countByImpUidContainsIgnoreCase(String impUid); // 결제 고유 번호 중복 확인

    List<PaymentEntity> findAllByEmail(String email);

}
