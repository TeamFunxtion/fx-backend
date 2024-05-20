package com.fx.funxtion.domain.notice.service;

import com.fx.funxtion.domain.notice.dto.*;
import com.fx.funxtion.domain.notice.entity.Notice;
import com.fx.funxtion.domain.notice.repository.NoticeRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
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

    public RsData<NoticeCreateResponse> getNoticeDetail(Long noticeId){
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);


        NoticeCreateResponse noticeCreateResponse = new NoticeCreateResponse(optionalNotice.get());

        return RsData.of("200","조회성공",noticeCreateResponse);
    }

    public RsData<NoticeUpdateResponse> updateNotice(NoticeUpdateRequest noticeUpdateRequest){
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeUpdateRequest.getNoticeId());
        if(optionalNotice.isEmpty()){
            return RsData.of("500","공지가 존재하지 않습니다");
        }


        Notice n = optionalNotice.get();

        if(noticeUpdateRequest.getNoticeTitle() != null && !noticeUpdateRequest.getNoticeTitle().isEmpty()){
            n.setNoticeTitle(noticeUpdateRequest.getNoticeTitle());
        }else{
            return RsData.of("500","타이틀이 없습니다");
        }
        if(noticeUpdateRequest.getNoticeContent() != null && !noticeUpdateRequest.getNoticeContent().isEmpty()) {
            n.setNoticeContent(noticeUpdateRequest.getNoticeContent());
        }else {
            return RsData.of("500","내용이 없습니다");
        }

            noticeRepository.save(n);

        return RsData.of("200","공지 수정 성공",new NoticeUpdateResponse(n));

        }

        public RsData<NoticeDto> deleteNotice (Long id){
            noticeRepository.deleteById(id);

            return RsData.of("200","삭제 성공");
        }
    }



