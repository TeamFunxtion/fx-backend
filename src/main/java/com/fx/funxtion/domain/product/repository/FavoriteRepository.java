package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Favorite findByUserIdAndProductId(Long userId, Long productId);
}
