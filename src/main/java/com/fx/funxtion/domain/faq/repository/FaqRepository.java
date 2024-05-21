package com.fx.funxtion.domain.faq.repository;

import com.fx.funxtion.domain.faq.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findFaqsByOrderByIdDesc(Pageable pageable);
    Optional<Faq> findFaqById(Long id);
    void deleteById(Long id);
}
