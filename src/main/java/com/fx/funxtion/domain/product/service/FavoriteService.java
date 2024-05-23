package com.fx.funxtion.domain.product.service;


import com.fx.funxtion.domain.product.dto.FavoriteDto;
import com.fx.funxtion.domain.product.dto.FavoriteUpdateRequest;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Favorite;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.FavoriteRepository;
import com.fx.funxtion.domain.product.repository.ProductImageRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public Page<FavoriteDto> getMyFavorites(Long userId, Pageable pageable) {

        // 페이징 처리된 Favorites 가져오기
        Page<Favorite> userFavoritesPage = favoriteRepository.findAllByUserId(userId, pageable);

        // UserFavoritesDto 리스트 생성
        List<FavoriteDto> favoriteDtoList = new ArrayList<>();

        for (Favorite userFavorites : userFavoritesPage) {
            Optional<Product> product = productRepository.findById(userFavorites.getProduct().getId());
            if (product.isPresent()) {
                FavoriteDto dto = new FavoriteDto(userFavorites);
                favoriteDtoList.add(dto);
            }
        }
        // PageImpl 객체로 반환
        return new PageImpl<>(favoriteDtoList, pageable, userFavoritesPage.getTotalElements());
    }

    public Long updateFavorites(FavoriteUpdateRequest favoritesUpdateRequest) {
        Product product = productRepository.findById(favoritesUpdateRequest.getProductId())
                          .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        Favorite favoritesEx = favoriteRepository.findByUserIdAndProductId(favoritesUpdateRequest.getUserId(), favoritesUpdateRequest.getProductId());
        Favorite fav;

        if(favoritesEx == null) {
            Favorite favorite = Favorite.builder()
                    .userId(favoritesUpdateRequest.getUserId())
                    .product(product)
                    .build();
            fav = favoriteRepository.save(favorite);
            return fav.getId();
        } else {
            favoriteRepository.delete(favoritesEx);
            return null;
        }

    }
}
