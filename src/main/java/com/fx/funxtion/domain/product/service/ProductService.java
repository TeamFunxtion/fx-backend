package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public RsData<ProductCreateResponse> createProduct(ProductCreateRequest productCreateRequest) {
        Product product = Product.builder()
                .storeId(productCreateRequest.getStoreId())
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

    public RsData<List<ProductDto>> getProductList() {
        List<ProductDto> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return RsData.of("200", "목록 조회 성공!", list);
    }

    public RsData<ProductDetailResponse> getProductDetail(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(product -> RsData.of("200", "상품 조회 성공!", new ProductDetailResponse(product)))
                .orElseGet(() -> RsData.of("500", "상품 조회 실패!"));
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
}
