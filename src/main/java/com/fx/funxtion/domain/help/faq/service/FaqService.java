package com.fx.funxtion.domain.help.faq.service;

import com.fx.funxtion.domain.help.faq.dto.*;
import com.fx.funxtion.domain.help.faq.entity.Faq;
import com.fx.funxtion.domain.help.faq.repository.FaqRepository;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    @Transactional
    public Page<FaqDto> findFaq(Pageable pageable, int pageNo, int pageSize) {
        pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<FaqDto> list = faqRepository.findAll(pageable).map(FaqDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

    @Transactional
    public RsData<FaqCreateResponse> createFaq(FaqCreateRequest faqCreateRequest) {
        Faq faq = Faq.builder()
                .faqTitle(faqCreateRequest.getFaqTitle())
                .faqContent(faqCreateRequest.getFaqContent())
                .build();

        System.out.println(faq.getFaqTitle());
        System.out.println(faq.getFaqOrder());

        faqRepository.save(faq);

        Optional<Faq> optionalFaq = faqRepository.findById(faq.getId());

        return optionalFaq.map(f -> RsData.of("200", "새글 등록 성공!", new FaqCreateResponse(f)))
                .orElseGet(() -> RsData.of("500", "새글 등록 실패!"));
    }
    public RsData<FaqCreateResponse> getFaqDetail(Long id) {
        Optional<Faq> optionalFaq = faqRepository.findById(id);

        FaqCreateResponse faqCreateResponse = new FaqCreateResponse(optionalFaq.get());

        return RsData.of("200", "조회성공", faqCreateResponse);
    }
    public RsData<FaqUpdateResponse> updateFaq(FaqUpdateRequest faqUpdateRequest) {
        Optional<Faq> optionalFaq = faqRepository.findById(faqUpdateRequest.getId());
        if (optionalFaq.isEmpty()){
            return  RsData.of("500", "수정할 FAQ를 찾을 수 없습니다.");
        }
        Faq faq = optionalFaq.get();

        if(faqUpdateRequest.getFaqTitle() != null && !faqUpdateRequest.getFaqTitle().isEmpty()){
            faq.setFaqTitle(faqUpdateRequest.getFaqTitle());
        }else{
            return RsData.of("500","제목이 없습니다");
        }
        if(faqUpdateRequest.getFaqContent() != null && !faqUpdateRequest.getFaqContent().isEmpty()){
            faq.setFaqContent(faqUpdateRequest.getFaqContent());
        }else {
            return RsData.of("500","내용이 없습니다");
        }
        faqRepository.save(faq);
        return RsData.of("200", "FAQ 수정 성공", new FaqUpdateResponse(faq));
    }
    public RsData<FaqDto> deleteFaq (Long id){
        faqRepository.deleteById(id);

        return RsData.of("200","삭제 성공");
    }

}




