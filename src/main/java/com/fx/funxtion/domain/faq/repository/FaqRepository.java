package com.fx.funxtion.domain.faq.repository;

import com.fx.funxtion.domain.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FaqRepository extends JpaRepository<Faq, Long> {

}
