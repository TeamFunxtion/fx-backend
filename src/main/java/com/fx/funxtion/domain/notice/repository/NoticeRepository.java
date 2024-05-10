package com.fx.funxtion.domain.notice.repository;

import com.fx.funxtion.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {



     Page<Notice> findNoticeByOrderByCreateDateDesc(Pageable pageable);

}
