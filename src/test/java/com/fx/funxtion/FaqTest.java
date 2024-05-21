package com.fx.funxtion;

import com.fx.funxtion.domain.faq.dto.FaqCreateRequest;
import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.repository.FaqRepository;
import com.fx.funxtion.domain.faq.service.FaqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class FaqTest {
    @Autowired
    private FaqService faqService;


    @Test
    public void FAQ등록(){
        FaqCreateRequest faqCreateRequest = new FaqCreateRequest();
        faqCreateRequest.setFaqContent("테스트내용");
        faqCreateRequest.setFaqTitle("테스트제목");

        faqService.createFaq(faqCreateRequest);
    }
}
