package com.fx.funxtion.domain.help.notice.contoller;


import com.fx.funxtion.domain.help.notice.dto.*;
import com.fx.funxtion.domain.help.notice.service.NoticeService;
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

    /**
     * 공지사항 목록 조회
     * @param pageNo
     * @param pageable
     * @return Page<NoticeDto>
     */
    @GetMapping("")
    public Page<NoticeDto> selectList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable) {
        int pageSize = 10;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<NoticeDto> pageNotice = null;

        try {
            pageNotice = noticeService.getSelectPage(pageable,pageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  pageNotice;
    }

    /**
     * 공지사항 수정
     * @param noticeUpdateRequest
     * @return RsData<NoticeUpdateResponse>
     */
    @PatchMapping("")
    public RsData<NoticeUpdateResponse> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest){
        try {
            RsData<NoticeUpdateResponse> noticeUpdateResponse = noticeService.updateNotice(noticeUpdateRequest);
            return RsData.of(noticeUpdateResponse.getResultCode(), noticeUpdateResponse.getMsg() , noticeUpdateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 공지사항 생성
     * @param noticeCreateRequest
     * @return RsData<NoticeCreateResponse>
     */
    @PostMapping("")
    public RsData<NoticeCreateResponse> createNotice(@RequestBody NoticeCreateRequest noticeCreateRequest) {
        try {
            RsData<NoticeCreateResponse> noticeCreateResponse = noticeService.createNotice(noticeCreateRequest);
            return  RsData.of(noticeCreateResponse.getResultCode() , noticeCreateResponse.getMsg(), noticeCreateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 공지사항 상세 조회
     * @param id
     * @return RsData<NoticeCreateResponse>
     */
    @GetMapping("/{id}")
    public RsData<NoticeCreateResponse> getNoticeDetail(@PathVariable(name="id") Long id ) {
        try {
            RsData<NoticeCreateResponse> noticeCreateResponse = noticeService.getNoticeDetail(id);
            return RsData.of(noticeCreateResponse.getResultCode(),noticeCreateResponse.getMsg(),noticeCreateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 공지사항 삭제
     * @param id
     * @return RsData<NoticeDto>
     */
    @DeleteMapping("/{id}")
    public RsData<NoticeDto> NoticeDelete(@PathVariable(name="id") Long id) {
        try {
            RsData<NoticeDto> noticeDto = noticeService.deleteNotice(id);
            return RsData.of(noticeDto.getResultCode(),noticeDto.getMsg());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
