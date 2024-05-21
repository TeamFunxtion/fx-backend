package com.fx.funxtion.domain.favorites.repository;

import com.fx.funxtion.domain.favorites.entity.UserFavorites;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Long> {

    Page<UserFavorites> findAllByUserId(Long userId, Pageable pageable);
    UserFavorites findByUserIdAndProductId(Long userId, Long productId);
}
