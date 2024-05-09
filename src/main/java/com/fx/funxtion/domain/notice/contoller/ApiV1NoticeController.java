package com.fx.funxtion.domain.notice.contoller;


import com.fx.funxtion.domain.notice.dto.NoticeDto;
import com.fx.funxtion.domain.notice.service.NoticeService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Slf4j
public class ApiV1NoticeController {
    private final NoticeService noticeService;


    @GetMapping("") // /api/v1/notices
    public List<NoticeDto> selectiList(){

        return noticeService.getselectList();
    }

}
