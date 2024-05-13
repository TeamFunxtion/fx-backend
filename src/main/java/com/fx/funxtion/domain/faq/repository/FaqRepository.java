package com.fx.funxtion.domain.faq.repository;

import com.fx.funxtion.domain.faq.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FaqRepository extends JpaRepository<Faq, Long> {
    Page<Faq> findFaqsByOrderByIdDesc(Pageable pageable);
}
