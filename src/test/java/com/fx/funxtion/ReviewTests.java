package com.fx.funxtion;

import com.fx.funxtion.domain.member.dto.ReviewCreateRequest;
import com.fx.funxtion.domain.member.dto.ReviewDto;
import com.fx.funxtion.domain.member.service.ReviewService;
import com.fx.funxtion.global.RsData.RsData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewTests {

    @Autowired
    private ReviewService reviewService;

    @Test
    public void 리뷰_등록() {
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest();
        reviewCreateRequest.setBuyerId(2L);
        reviewCreateRequest.setSellerId(1L);
        reviewCreateRequest.setProductId(1L);
        reviewCreateRequest.setContent("친절합니다.");
        reviewCreateRequest.setRating(5);

        RsData<ReviewDto> reviewDto =  reviewService.enrollReview(reviewCreateRequest);

        Assertions.assertThat(reviewDto.getResultCode()).isEqualTo("200");
    }
}
