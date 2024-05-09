package com.fx.funxtion.domain.notice.service;


import com.fx.funxtion.domain.notice.dto.NoticeDto;
import com.fx.funxtion.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<NoticeDto> getselectList() {

        return noticeRepository.findAll().stream().map(NoticeDto::new).toList();
    }
}
