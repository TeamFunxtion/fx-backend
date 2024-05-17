package com.fx.funxtion.domain.notice.service;

import com.fx.funxtion.domain.notice.dto.NoticeCreateRequest;
import com.fx.funxtion.domain.notice.dto.NoticeCreateResponse;
import com.fx.funxtion.domain.notice.dto.NoticeDto;
import com.fx.funxtion.domain.notice.entity.Notice;
import com.fx.funxtion.domain.notice.repository.NoticeRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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

    public RsData<NoticeCreateResponse> createNotice(NoticeCreateRequest noticeCreateRequest) {
        Notice notice = Notice.builder()
                .noticeContent(noticeCreateRequest.getNoticeContent())
                .noticeTitle(noticeCreateRequest.getNoticeTitle())
                .build()
                ;
        noticeRepository.save(notice);
        Optional<Notice> optionalNotice = noticeRepository.findById(notice.getId());

        return optionalNotice.map(q ->RsData.of("200","1:1 문의 등록 성공",new NoticeCreateResponse(q))).orElseGet(() -> RsData.of("500","1:1 문의 등록 실패"));
    }


}
