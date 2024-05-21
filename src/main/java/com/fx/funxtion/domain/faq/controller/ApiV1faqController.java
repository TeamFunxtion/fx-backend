package com.fx.funxtion.domain.faq.controller;

import com.fx.funxtion.domain.faq.dto.*;
import com.fx.funxtion.domain.faq.repository.FaqRepository;
import com.fx.funxtion.domain.faq.service.FaqService;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Slf4j
public class ApiV1faqController {
    private final FaqService faqService;
    private final FaqRepository faqRepository;

    @GetMapping("")
    public Page<FaqDto> getFaqs(
            @RequestParam(required = false, defaultValue = "0", value = "page")int pageNo,
            @PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        int pageSize = 10;

        Page<FaqDto> pageFaq;

        pageFaq = faqService.findFaq(pageable, pageNo, pageSize);

        return pageFaq;
    }
   @PostMapping("")
    public RsData<FaqCreateResponse> createFaq(@RequestBody FaqCreateRequest faqCreateRequest) {
        System.out.println(faqCreateRequest);

        RsData<FaqCreateResponse> faqCreateResponse = faqService.createFaq(faqCreateRequest);

        return RsData.of(faqCreateResponse.getResultCode(),faqCreateResponse.getMsg(),faqCreateResponse.getData());
   }
    @PatchMapping("/{id}")
    public RsData<FaqUpdateResponse> updateFaq(@PathVariable(name="id") Long id, @RequestBody FaqUpdateRequest faqUpdateRequest) {
        faqUpdateRequest.setId(id);
        return faqService.updateFaq(faqUpdateRequest);
    }
    @GetMapping("/{id}")
    public RsData<FaqCreateResponse> getFaq(@PathVariable(name="id") Long id) {
        RsData<FaqCreateResponse> faqCreateResponse = faqService.getFaqDetail(id);
        return RsData.of(faqCreateResponse.getResultCode(), faqCreateResponse.getMsg(),faqCreateResponse.getData());

    }
    @DeleteMapping("/{id}")
    public RsData<FaqDto> FaqDelete(@PathVariable(name="id") Long id){
        RsData<FaqDto> faqDto = faqService.deleteFaq(id);

        return RsData.of(faqDto.getResultCode(),faqDto.getMsg());
    }
}
