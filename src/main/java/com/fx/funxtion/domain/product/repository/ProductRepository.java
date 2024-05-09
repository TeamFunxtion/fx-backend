package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p set p.views = p.views + 1 where p.id = :id")
    int updateViews(@Param(value="id")Long id);
}