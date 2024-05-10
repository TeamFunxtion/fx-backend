package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p set p.views = p.views + 1 where p.id = :id")
    int updateViews(@Param(value="id")Long id);

    @Query(value = "select p " +
            "from Product p " +
            "where p.salesTypeId in ('SA01', 'SA02') " +
            "and p.statusTypeId = 'ST01' " +
            "and p.endTime <= current_date")
    List<Product> findAllAfterAuctionEndTime();

    Page<Product> findByProductTitleContaining(String keyword, Pageable pageable);
}