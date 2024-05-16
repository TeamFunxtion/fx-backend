package com.fx.funxtion.domain.safepayment.repository;

import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafePaymentsRepository extends JpaRepository<SafePayments, Long> {

    SafePayments findByProductIdAndSellerIdAndBuyerId(Long productId, Long sellerId, Long buyerId);
}
