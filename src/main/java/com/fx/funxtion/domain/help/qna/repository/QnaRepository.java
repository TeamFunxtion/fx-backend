package com.fx.funxtion.domain.help.qna.repository;

import com.fx.funxtion.domain.help.qna.entity.Qna;
import com.fx.funxtion.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface QnaRepository extends JpaRepository<Qna, Long> {



    Page<Qna> findByMember(Member member, Pageable pageable);
    Page<Qna> findAllBy(Pageable pageable);
    Page<Qna> findByQnaAnswerNull(Pageable pageable);
}
