package com.fx.funxtion.domain.product.repository;

import com.fx.funxtion.domain.product.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByUserIdAndProductId(Long userId, Long productId);
}
