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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ApiV1ReviewController {

    private final ReviewService reviewService;


    @PostMapping("")
    public RsData<ReviewDto> enrollReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        return reviewService.enrollReview(reviewCreateRequest);
    }

    @GetMapping("")
    public RsData<Page<ReviewDetailDto>> selectReviewListWithPageable(
            @RequestParam(name = "sellerId") Long sellerId,
            @RequestParam(name = "pageNo") int pageNo
    ) {

        pageNo = pageNo == 0 ? pageNo : pageNo-1;

        Pageable pageable = PageRequest.of(pageNo, 10, Sort.Direction.DESC);

        return reviewService.selectReviewListWithPageable(sellerId, pageable);
    }
}
