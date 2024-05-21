package com.fx.funxtion.domain.favorites.service;


import com.fx.funxtion.domain.favorites.dto.UserFavoritesDto;
import com.fx.funxtion.domain.favorites.dto.UserFavoritesUpdateRequest;
import com.fx.funxtion.domain.favorites.entity.UserFavorites;
import com.fx.funxtion.domain.favorites.repository.UserFavoritesRepository;
import com.fx.funxtion.domain.product.entity.Product;
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
public class UserFavoritesService {

    private final UserFavoritesRepository userFavoritesRepository;
    private final ProductRepository productRepository;

    public Page<UserFavoritesDto> getMyFavorites(Long userId, Pageable pageable) {

        // 페이징 처리된 UserFavorites 가져오기
        Page<UserFavorites> userFavoritesPage = userFavoritesRepository.findAllByUserId(userId, pageable);

        // UserFavoritesDto 리스트 생성
        List<UserFavoritesDto> userFavoritesDtoList = new ArrayList<>();

        for (UserFavorites userFavorites : userFavoritesPage) {
            Optional<Product> product = productRepository.findById(userFavorites.getProduct().getId());
            if (product.isPresent()) {
                UserFavoritesDto dto = new UserFavoritesDto(userFavorites.getId(), userFavorites.getUserId(), product.get(), userFavorites.getCreateDate());
                userFavoritesDtoList.add(dto);
            }
        }
        // PageImpl 객체로 반환
        return new PageImpl<>(userFavoritesDtoList, pageable, userFavoritesPage.getTotalElements());
    }

    public Long updateFavorites(UserFavoritesUpdateRequest userFavoritesUpdateRequest) {
        Product product = productRepository.findById(userFavoritesUpdateRequest.getProductId())
                          .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        UserFavorites userFavoritesEx = userFavoritesRepository.findByUserIdAndProductId(userFavoritesUpdateRequest.getUserId(), userFavoritesUpdateRequest.getProductId());
        UserFavorites uf;

        if(userFavoritesEx == null) {
            UserFavorites userFavorites = UserFavorites.builder()
                    .userId(userFavoritesUpdateRequest.getUserId())
                    .product(product)
                    .build();
            uf = userFavoritesRepository.save(userFavorites);
            return uf.getId();
        } else {
            userFavoritesRepository.delete(userFavoritesEx);
            return null;
        }

    }
}
