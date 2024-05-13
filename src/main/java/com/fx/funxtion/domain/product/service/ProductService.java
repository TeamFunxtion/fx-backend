package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.entity.Favorite;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.FavoriteRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    public RsData<ProductCreateResponse> createProduct(ProductCreateRequest productCreateRequest) {
        Member member = memberRepository.findById(productCreateRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Product product = Product.builder()
                .member(member)
                .categoryId(productCreateRequest.getCategoryId())
                .productTitle(productCreateRequest.getProductTitle())
                .productDesc(productCreateRequest.getProductDesc())
                .productPrice(productCreateRequest.getProductPrice())
                .qualityTypeId(productCreateRequest.getQualityTypeId())
                .salesTypeId(productCreateRequest.getSalesTypeId())
                .statusTypeId(ProductStatusType.ST01.name())
                .location(productCreateRequest.getLocation())
                .currentPrice(productCreateRequest.getProductPrice())
                .coolPrice(productCreateRequest.getCoolPrice())
                .endTime(LocalDateTime.now().plusDays(productCreateRequest.getEndDays()))
                .build();

        productRepository.save(product);

        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        return optionalProduct.map(p -> RsData.of("200", "상품 등록 성공!", new ProductCreateResponse(p)))
                .orElseGet(() -> RsData.of("500", "상품 등록 실패!"));
    }

    @Transactional
    public Page<ProductDto> searchByKeyword(String keyword, Pageable pageable, int pageNo, int pageSize) {
        pageable = PageRequest.of(pageNo,pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<ProductDto> list = productRepository.findByProductTitleContainingAndStatusTypeId(keyword, "ST01", pageable)
                .map(ProductDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

    public Page<ProductDto> searchByCategory(String category, Pageable pageable, int pageNo, int pageSize) {
        pageable = PageRequest.of(pageNo,pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<ProductDto> list = productRepository.findByCategoryIdAndStatusTypeId(category, "ST01", pageable)
                .map(ProductDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

    public RsData<List<ProductDto>> getProductList() {
        List<ProductDto> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return RsData.of("200", "목록 조회 성공!", list);
    }

    public RsData<ProductDetailResponse> getProductDetail(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()) {
            return RsData.of("500", "상품 조회 실패!");
        }

        ProductDetailResponse productDetailResponse = new ProductDetailResponse(optionalProduct.get());
        if(userId != null) {
            Favorite favor = favoriteRepository.findByUserIdAndProductId(userId, productId);
            productDetailResponse.setFavorite(favor != null);
        }

        return RsData.of("200", "상품 조회 성공!", productDetailResponse);
    }

    public RsData<ProductUpdateResponse> updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        System.out.println(optionalProduct.isEmpty());

        if(optionalProduct.isEmpty()) {
            return RsData.of("500", "상품이 존재하지 않습니다!");
        }

        Product p = optionalProduct.get();

        if(productUpdateRequest.getCategoryId() != null && !productUpdateRequest.getCategoryId().isEmpty()) {
            p.setCategoryId(productUpdateRequest.getCategoryId());
        }
        if(productUpdateRequest.getProductTitle() != null && !productUpdateRequest.getProductTitle().isEmpty()) {
            p.setProductTitle(productUpdateRequest.getProductTitle());
        }
        if(productUpdateRequest.getProductDesc() != null && !productUpdateRequest.getProductDesc().isEmpty()) {
            p.setProductDesc(productUpdateRequest.getProductDesc());
        }
        if(productUpdateRequest.getProductPrice() != null ) {
            p.setProductPrice(productUpdateRequest.getProductPrice());
        }
        if(productUpdateRequest.getLocation() != null && !productUpdateRequest.getLocation().isEmpty()) {
            p.setLocation(productUpdateRequest.getLocation());
        }
        if(productUpdateRequest.getQualityTypeId() != null && !productUpdateRequest.getQualityTypeId().isEmpty()) {
            p.setQualityTypeId(productUpdateRequest.getQualityTypeId());
        }
        if(productUpdateRequest.getSalesTypeId() != null && !productUpdateRequest.getSalesTypeId().isEmpty()) {
            p.setSalesTypeId(productUpdateRequest.getSalesTypeId());
        }
        if(productUpdateRequest.getStatusTypeId() != null && !productUpdateRequest.getStatusTypeId().isEmpty()) {
            p.setStatusTypeId(productUpdateRequest.getStatusTypeId());
        }

        productRepository.save(p);

        return RsData.of("200", "상품 수정 성공!", new ProductUpdateResponse(p));
    }

    @Transactional
    public int increaseViews(Long id) {
        return productRepository.increaseViews(id);
    }


    public boolean updateFavorite(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId);

        if(favorite != null) {
            favoriteRepository.delete(favorite);
            return false;
        } else {
            Product product = productRepository.findById(productId).get();

            favorite = Favorite.builder()
                    .userId(userId)
                    .product(product)
                    .build();

            favoriteRepository.save(favorite);
            return true;
        }
    }
}
