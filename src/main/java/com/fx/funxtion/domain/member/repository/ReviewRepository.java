package com.fx.funxtion.domain.member.repository;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.entity.Review;
import com.fx.funxtion.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllBySellerId(Long sellerId, Pageable pageable);

    Optional<Review> findByBuyerAndSellerIdAndProduct(Member buyer, Long sellerId, Product product);

    List<Review> findAllBySellerId(Long sellerId);

    @Query("SELECT avg(ur.rating) FROM Review ur WHERE ur.sellerId = :sellerId")
    Double findByAvgRatingBySellerId(@Param("sellerId") Long sellerId);
}
