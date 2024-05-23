package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByProductId(Long productId);

    @Query("SELECT DISTINCT b.product.id FROM Bid b WHERE b.member.id = :id")
    List<Long> findWithProductUsingJoinByMember(@Param("id") Long id);
}
