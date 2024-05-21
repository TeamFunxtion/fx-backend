package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findAllByUserId(Long userId, Pageable pageable);
    Favorite findByUserIdAndProductId(Long userId, Long productId);
}
