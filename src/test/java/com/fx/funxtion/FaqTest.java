package com.fx.funxtion;

import com.fx.funxtion.domain.help.faq.dto.FaqCreateRequest;
import com.fx.funxtion.domain.help.faq.service.FaqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FaqTest {
    @Autowired
    private FaqService faqService;


    @Test
    public void FAQ등록() throws Exception  {
        FaqCreateRequest faqCreateRequest = new FaqCreateRequest();
        faqCreateRequest.setFaqContent("테스트내용");
        faqCreateRequest.setFaqTitle("테스트제목");

        faqService.createFaq(faqCreateRequest);
    }
}
