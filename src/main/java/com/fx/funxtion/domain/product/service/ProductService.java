package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public RsData<ProductDto> createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .storeId(productDto.getStoreId())
                .categoryId(productDto.getCategoryId())
                .productTitle(productDto.getProductTitle())
                .productDesc(productDto.getProductDesc())
                .productPrice(productDto.getProductPrice())
                .qualityTypeId(productDto.getQualityTypeId())
                .salesTypeId(productDto.getSalesTypeId())
                .statusTypeId(ProductStatusType.ST01.name())
                .location(productDto.getLocation())
                .build();
        productRepository.save(product);

        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        return optionalProduct.map(p -> RsData.of("200", "상품 등록 성공!", new ProductDto(p)))
                .orElseGet(() -> RsData.of("500", "상품 등록 실패!"));
    }

    public List<ProductDto> getProductList() {
        List<ProductDto> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(p -> new ProductDto(p))
                .collect(Collectors.toList());
        return list;
    }

    public RsData<ProductDto> getProduct(long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(product -> RsData.of("200", "상품 조회 성공!", new ProductDto(product)))
                .orElseGet(() -> RsData.of("500", "상품 조회 실패!"));

    }

    public RsData<ProductDto> updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        System.out.println(optionalProduct.isEmpty());
        if(optionalProduct.isEmpty()) {
            return RsData.of("500", "상품이 존재하지 않습니다!");
        }

        Product p = optionalProduct.get();

        if(productDto.getCategoryId() != null && !productDto.getCategoryId().equals("")) {
            p.setCategoryId(productDto.getCategoryId());
        }
        if(productDto.getProductTitle() != null && !productDto.getProductTitle().equals("")) {
            p.setProductTitle(productDto.getProductTitle());
        }
        if(productDto.getProductDesc() != null && !productDto.getProductDesc().equals("")) {
            p.setProductDesc(productDto.getProductDesc());
        }
        if(productDto.getProductPrice() > 0) {
            p.setProductPrice(productDto.getProductPrice());
        }
        if(productDto.getLocation() != null && !productDto.getLocation().equals("")) {
            p.setLocation(productDto.getLocation());
        }
        if(productDto.getQualityTypeId() != null && !productDto.getQualityTypeId().equals("")) {
            p.setQualityTypeId(productDto.getQualityTypeId());
        }
        if(productDto.getSalesTypeId() != null && !productDto.getSalesTypeId().equals("")) {
            p.setSalesTypeId(productDto.getSalesTypeId());
        }
        if(productDto.getStatusTypeId() != null && !productDto.getStatusTypeId().equals("")) {
            p.setStatusTypeId(productDto.getStatusTypeId());
        }

        productRepository.save(p);

        return RsData.of("200", "상품 수정 성공!", new ProductDto(p));
    }
}
