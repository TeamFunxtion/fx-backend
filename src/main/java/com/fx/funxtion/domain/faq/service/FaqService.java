package com.fx.funxtion.domain.faq.service;

import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.repository.FaqRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
    private final  FaqRepository faqRepository;

    public List<FaqDto> findAll() {
        return faqRepository.findAll().stream().map(FaqDto::new).toList();
    }
}




