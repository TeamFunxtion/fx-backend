package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByProductId(Long productId);
}
