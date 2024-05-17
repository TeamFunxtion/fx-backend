package com.fx.funxtion.domain.faq.controller;

import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.service.FaqService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Slf4j
public class ApiV1faqController {
    private final FaqService faqService;

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


}
