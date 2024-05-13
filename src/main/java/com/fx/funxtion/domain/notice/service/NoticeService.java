package com.fx.funxtion.domain.notice.service;


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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public Page<NoticeDto> getselectpage(Pageable pageable,int pageNo , int pageSize) {
        pageable = PageRequest.of(pageNo,pageSize,Sort.by(Sort.Direction.DESC,"id"));
        Page<NoticeDto> list =noticeRepository.findAll(pageable).map(NoticeDto::new);

        return list;
    }
//    public RsData<List<NoticeDto>> getNoticeList(){
//        List<NoticeDto> list = noticeRepository.findAll().stream().map(NoticeDto::new).collect(Collectors.toList());
//        return RsData.of("200", "목록 조회 성공",list);
//    }


}
