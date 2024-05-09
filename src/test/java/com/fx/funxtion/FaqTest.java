package com.fx.funxtion;

import com.fx.funxtion.domain.faq.dto.FaqDto;
import com.fx.funxtion.domain.faq.repository.FaqRepository;
import com.fx.funxtion.domain.faq.service.FaqService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class FaqTest {
    @Autowired
    private FaqService faqService;

    @Test
    public void test(){

        List<FaqDto> result = faqService.findAll();

        for(int i=0; i<result.size(); i++){
            System.out.println(result.get(i));
        }
    }
}
