package com.fx.funxtion.domain.notice.contoller;


import com.fx.funxtion.domain.notice.dto.NoticeCreateRequest;
import com.fx.funxtion.domain.notice.dto.NoticeCreateResponse;
import com.fx.funxtion.domain.notice.dto.NoticeDto;
import com.fx.funxtion.domain.notice.service.NoticeService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Slf4j
public class ApiV1NoticeController {
    private final NoticeService noticeService;

    @GetMapping("") // /api/v1/notices
    public Page<NoticeDto> selectList(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                      @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable) {

        int pageSize = 10;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<NoticeDto> pageNotice;

        pageNotice = noticeService.getSelectPage(pageable,pageNo,pageSize);

        return  pageNotice;

    }

    @GetMapping("/update")
    public RsData<NoticeCreateResponse> getNoticeDeatils(@PathVariable(name="id") Long id){
        return null;
    }

    @PostMapping("")
    public RsData<NoticeCreateResponse> createNotice(@RequestBody NoticeCreateRequest noticeCreateRequest) {

    RsData<NoticeCreateResponse> noticeCreateResponse = noticeService.createNotice(noticeCreateRequest);


    return  RsData.of(noticeCreateResponse.getResultCode() , noticeCreateResponse.getMsg(), noticeCreateResponse.getData());
    }




}
