package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.product.entity.Bid;
import com.fx.funxtion.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByProductId(Long productId);

    @Query("SELECT DISTINCT b.product.id FROM Bid b WHERE b.member.id = :id")
    List<Long> findWithProductUsingJoinByMember(@Param("id") Long id);

    Optional<Bid> findByProductAndMemberAndBidPrice(Product product, Member member, Long bidPrice);

    Optional<Bid> findByProductAndMemberAndBidPriceGreaterThan(Product product, Member member, Long bidPrice);
}
