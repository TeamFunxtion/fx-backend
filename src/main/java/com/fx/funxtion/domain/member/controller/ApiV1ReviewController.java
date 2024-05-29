package com.fx.funxtion.domain.member.controller;

import com.fx.funxtion.domain.member.dto.ReviewCreateRequest;
import com.fx.funxtion.domain.member.dto.ReviewDetailDto;
import com.fx.funxtion.domain.member.dto.ReviewDto;
import com.fx.funxtion.domain.member.service.ReviewService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ApiV1ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param reviewCreateRequest
     * @return RsData<ReviewDto>
     */
    @PostMapping("")
    public RsData<ReviewDto> enrollReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        try {
            return reviewService.enrollReview(reviewCreateRequest);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 리뷰 목록 조회
     * @param sellerId
     * @param pageNo
     * @param pageable
     * @return RsData<Page<ReviewDetailDto>>
     */
    @GetMapping("")
    public RsData<Page<ReviewDetailDto>> selectReviewListWithPageable(
            @RequestParam(name = "sellerId") Long sellerId,
            @RequestParam(required = false, defaultValue = "0", value = "pageNo") int pageNo,
            @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        pageNo = pageNo == 0 ? pageNo : pageNo-1;
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), pageable.getSort());
        try {
            return reviewService.selectReviewListWithPageable(sellerId, pageable);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
