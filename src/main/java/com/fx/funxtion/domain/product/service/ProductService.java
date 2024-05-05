package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void registerProduct(ProductDto productDto) {
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
    }

    public List<ProductDto> getProductList() {
        List<ProductDto> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(p -> new ProductDto(p))
                .collect(Collectors.toList());
        return list;
    }

    public ProductDto getProduct(long productId) {
        Product p = productRepository.findById(productId).get();
        ProductDto productDto = new ProductDto(p);

        return productDto;
    }

    public void editProduct(ProductDto productDto) {

        Product p = productRepository.findById(productDto.getId()).get();

        p.setCategoryId(productDto.getCategoryId());
        p.setProductTitle(productDto.getProductTitle());
        p.setProductDesc(productDto.getProductDesc());
        p.setProductPrice(productDto.getProductPrice());
        p.setLocation(productDto.getLocation());
        p.setQualityTypeId(productDto.getQualityTypeId());
        p.setSalesTypeId(productDto.getSalesTypeId());

        productRepository.save(p);
    }

    public void changeStatus(ProductDto productDto) {

        Product p = productRepository.findById(productDto.getId()).get();

        p.setStatusTypeId(productDto.getStatusTypeId());

        productRepository.save(p);
    }
}
