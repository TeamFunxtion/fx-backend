package com.fx.funxtion.domain.faq.service;

import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.entity.Faq;
import com.fx.funxtion.domain.faq.repository.FaqRepository;
import com.fx.funxtion.domain.product.dto.ProductDto;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {
    private final  FaqRepository faqRepository;

    @Transactional
    public Page<FaqDto> findFaq( Pageable pageable, int pageNo, int pageSize) {
        pageable = PageRequest.of(pageNo,pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<FaqDto> list = faqRepository.findAll(pageable).map(FaqDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

}




