package com.fx.funxtion.domain.help.qna.controller;


import com.fx.funxtion.domain.help.qna.dto.*;
import com.fx.funxtion.domain.help.qna.service.QnaService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/qnas")
@RequiredArgsConstructor
@Slf4j
public class ApiV1QnaController {

    private final QnaService qnaService;

    /**
     * QNA 목록 조회(사용자용)
     * @param userId
     * @param pageNo
     * @param pageable
     * @return Page<QnaDto>
     */
    @GetMapping("")
    public Page<QnaDto> selectQna(
            @RequestParam("id") Long userId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable){
        int pageSize = 5;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<QnaDto> pageQna = null;
        try {
            pageQna = qnaService.getSelectPage(userId,pageable,pageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageQna;
    }

    /**
     * QNA 목록 조회(관리자용)
     * @param pageNo
     * @param pageable
     * @return Page<QnaDto>
     */
    @GetMapping("/manager")
    public Page<QnaDto> selectManagerPage(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable) {
        int pageSize = 5;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<QnaDto> pageQna = null;
        try {
            pageQna = qnaService.getSelectManagerPage(pageable, pageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageQna;
    }

    /**
     * QNA 생성
     * @param qnaCreateRequest
     * @return RsData<QnaCreateResponse>
     */
    @PostMapping("")
    public RsData<QnaCreateResponse> createQna(@RequestBody QnaCreateRequest qnaCreateRequest) {
        try {
            RsData<QnaCreateResponse> qnaCreateResponse = qnaService.createQna(qnaCreateRequest);
            return RsData.of(qnaCreateResponse.getResultCode(), qnaCreateResponse.getMsg(), qnaCreateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * QNA 수정
     * @param qnaUpdateRequest
     * @return RsData<QnaUpdateResponse>
     */
    @PatchMapping("")
    public RsData<QnaUpdateResponse> updateQna(@RequestBody QnaUpdateRequest qnaUpdateRequest) {
        try {
            RsData<QnaUpdateResponse> qnaUpdateResponse = qnaService.updateQna(qnaUpdateRequest);
            return RsData.of(qnaUpdateResponse.getResultCode(), qnaUpdateResponse.getMsg(), qnaUpdateResponse.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
