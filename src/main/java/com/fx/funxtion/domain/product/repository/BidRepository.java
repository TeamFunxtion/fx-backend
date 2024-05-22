package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByProductId(Long productId);

    @Query("SELECT b FROM Bid b join fetch b.member m where m.id = :id")
    Page<ProductDto> findWithProductUsingJoinByMember(@Param("id") Long id, Pageable pageable);

}
