package com.fx.funxtion.domain.help.faq.controller;

import com.fx.funxtion.domain.help.faq.dto.*;
import com.fx.funxtion.domain.help.faq.repository.FaqRepository;
import com.fx.funxtion.domain.help.faq.service.FaqService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Slf4j
public class ApiV1FaqController {
    private final FaqService faqService;

    /**
     * FAQ 목록 조회
     * @param pageNo
     * @param pageable
     * @return Page<FaqDto>
     */
    @GetMapping("")
    public Page<FaqDto> getFaqs(
            @RequestParam(required = false, defaultValue = "0", value = "page")int pageNo,
            @PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        int pageSize = 10;

        Page<FaqDto> pageFaq = null;

        try {
            pageFaq = faqService.findFaq(pageable, pageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageFaq;
    }

    /**
     * FAQ 생성
     * @param faqCreateRequest
     * @return RsData<FaqCreateResponse>
     */
    @PostMapping("")
    public RsData<FaqCreateResponse> createFaq(@RequestBody FaqCreateRequest faqCreateRequest) {
        try {
            RsData<FaqCreateResponse> faqCreateResponse = faqService.createFaq(faqCreateRequest);
            return RsData.of(faqCreateResponse.getResultCode(),faqCreateResponse.getMsg(),faqCreateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * FAQ 수정
     * @param id
     * @param faqUpdateRequest
     * @return RsData<FaqUpdateResponse>
     */
    @PatchMapping("/{id}")
    public RsData<FaqUpdateResponse> updateFaq(@PathVariable(name="id") Long id, @RequestBody FaqUpdateRequest faqUpdateRequest) {
        try {
            faqUpdateRequest.setId(id);
            return faqService.updateFaq(faqUpdateRequest);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * FAQ 상세 조회
     * @param id
     * @return RsData<FaqCreateResponse>
     */
    @GetMapping("/{id}")
    public RsData<FaqCreateResponse> getFaq(@PathVariable(name="id") Long id) {
        try {
            RsData<FaqCreateResponse> faqCreateResponse = faqService.getFaqDetail(id);
            return RsData.of(faqCreateResponse.getResultCode(), faqCreateResponse.getMsg(),faqCreateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * FAQ 삭제
     * @param id
     * @return RsData<FaqDto>
     */
    @DeleteMapping("/{id}")
    public RsData<FaqDto> FaqDelete(@PathVariable(name="id") Long id){
        try {
            RsData<FaqDto> faqDto = faqService.deleteFaq(id);
            return RsData.of(faqDto.getResultCode(),faqDto.getMsg());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
