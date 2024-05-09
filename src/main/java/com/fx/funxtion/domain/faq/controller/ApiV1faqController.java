package com.fx.funxtion.domain.faq.controller;

import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.service.FaqService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
@Slf4j
public class ApiV1faqController {
    private final FaqService faqService;

    @GetMapping("")
    public List<FaqDto> getFaqs() {
        return faqService.findAll();
    }


}
