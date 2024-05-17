package com.fx.funxtion.domain.qna.repository;

import com.fx.funxtion.domain.qna.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QnaRepository extends JpaRepository<Qna, Long> {

    Page<Qna> findByUserId(Long userId, Pageable pageable);


}
