package com.fx.funxtion.domain.safepayment.repository;

import com.fx.funxtion.domain.safepayment.entity.SafePaymentStatus;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SafePaymentsRepository extends JpaRepository<SafePayments, Long> {

    SafePayments findByProductIdAndSellerIdAndBuyerId(Long productId, Long sellerId, Long buyerId);
    List<SafePayments> findBySellerIdAndBuyerId(Long sellerId, Long buyerId);
    SafePayments findBySellerIdAndBuyerIdAndStatus(Long sellerId, Long buyerId, SafePaymentStatus status);
    @Query("SELECT sp FROM SafePayments sp WHERE sp.sellerId = :sellerId AND sp.buyerId = :buyerId AND sp.status = :status ORDER BY sp.id ASC")
    SafePayments findFirstBySellerIdAndBuyerIdAndStatusOrderByIdAsc(
            @Param("sellerId") Long sellerId,
            @Param("buyerId") Long buyerId,
            @Param("status") SafePaymentStatus status);


}
