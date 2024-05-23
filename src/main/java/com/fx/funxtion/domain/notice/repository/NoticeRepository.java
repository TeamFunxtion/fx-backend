package com.fx.funxtion.domain.notice.repository;

import com.fx.funxtion.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NoticeRepository extends JpaRepository<Notice, Long> {



}
