package com.fx.funxtion;

import com.fx.funxtion.domain.help.notice.service.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NoticeTest {

    @Autowired
    private NoticeService noticeService;

    @Test
    public void test() {

//        List<NoticeDto> result =  noticeService.getselectList();

//        for(int i=0; i < result.size() ; i++) {
//            System.out.println(result.get(i));
//        }


    }
}
