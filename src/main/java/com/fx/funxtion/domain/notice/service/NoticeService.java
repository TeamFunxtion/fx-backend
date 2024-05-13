package com.fx.funxtion.domain.notice.service;

import com.fx.funxtion.domain.notice.dto.NoticeDto;
import com.fx.funxtion.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<NoticeDto> getSelectPage(Pageable pageable,int pageNo , int pageSize) {
        pageable = PageRequest.of(pageNo,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        Page<NoticeDto> list =noticeRepository.findAll(pageable).map(NoticeDto::new);

        return list;
    }
}
