package com.fx.funxtion.domain.qna.controller;


import com.fx.funxtion.domain.notice.dto.NoticeUpdateResponse;
import com.fx.funxtion.domain.qna.dto.*;
import com.fx.funxtion.domain.qna.service.QnaService;
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


    @GetMapping("")
    public Page<QnaDto> selectQna(
            @RequestParam("id") Long userId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable){

        int pageSize = 5;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<QnaDto> pageQna;

        pageQna = qnaService.getSelectPage(userId,pageable,pageNo,pageSize);

        return pageQna;
    }

    @GetMapping("/manager")
    public Page<QnaDto> selectManagerPage(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @PageableDefault(size = 5,sort ="id",direction = Sort.Direction.DESC) Pageable pageable) {

        int pageSize = 5;
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        Page<QnaDto> pageQna;

        pageQna = qnaService.getSelectManagerPage(pageable, pageNo, pageSize);

        return pageQna;

    }


    @PostMapping("")
    public RsData<QnaCreateResponse> createQna(@RequestBody QnaCreateRequest qnaCreateRequest) {
        System.out.println(qnaCreateRequest);

        RsData<QnaCreateResponse> qnaCreateResponse = qnaService.createQna(qnaCreateRequest);


        return RsData.of(qnaCreateResponse.getResultCode(), qnaCreateResponse.getMsg(), qnaCreateResponse.getData());
    }

    @PatchMapping("")
    public RsData<QnaUpdateResponse> updateQna(@RequestBody QnaUpdateRequest qnaUpdateRequest) {
        RsData<QnaUpdateResponse> qnaUpdateResponse = qnaService.updateQna(qnaUpdateRequest);

        return RsData.of(qnaUpdateResponse.getResultCode(), qnaUpdateResponse.getMsg(), qnaUpdateResponse.getData());
    }

}
