package com.fx.funxtion.domain.notice.contoller;


import com.fx.funxtion.domain.notice.dto.*;
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

    // 전체 공지 조회
    @GetMapping("") // /api/v1/notices
    public Page<NoticeDto> selectList(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                      @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable) {

        int pageSize = 10;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<NoticeDto> pageNotice;

        pageNotice = noticeService.getSelectPage(pageable,pageNo,pageSize);

        return  pageNotice;

    }

    @PatchMapping("")
    public RsData<NoticeUpdateResponse> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest){
        RsData<NoticeUpdateResponse> noticeUpdateResponse = noticeService.updateNotice(noticeUpdateRequest);


        return RsData.of(noticeUpdateResponse.getResultCode(), noticeUpdateResponse.getMsg() , noticeUpdateResponse.getData());
    }


    //공지 생성
    @PostMapping("")
    public RsData<NoticeCreateResponse> createNotice(@RequestBody NoticeCreateRequest noticeCreateRequest) {

    RsData<NoticeCreateResponse> noticeCreateResponse = noticeService.createNotice(noticeCreateRequest);


    return  RsData.of(noticeCreateResponse.getResultCode() , noticeCreateResponse.getMsg(), noticeCreateResponse.getData());
    }


    // 수정할 공지 불러오기
    @GetMapping("/{id}")
    public RsData<NoticeCreateResponse> getNoticeDetail(@PathVariable(name="id") Long id ){
        RsData<NoticeCreateResponse> noticeCreateResponse = noticeService.getNoticeDetail(id);
        return RsData.of(noticeCreateResponse.getResultCode(),noticeCreateResponse.getMsg(),noticeCreateResponse.getData());

    }

    @DeleteMapping("/{id}")
    public RsData<NoticeDto> NoticeDelete(@PathVariable(name="id") Long id){
       RsData<NoticeDto> noticeDto = noticeService.deleteNotice(id);

        return RsData.of(noticeDto.getResultCode(),noticeDto.getMsg());

    }




}
