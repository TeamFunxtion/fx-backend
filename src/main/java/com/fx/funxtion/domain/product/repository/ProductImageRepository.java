package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
