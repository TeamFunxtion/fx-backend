package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.dto.ReviewCreateRequest;
import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.entity.Review;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.member.repository.ReviewRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public RsData<ReviewDto> enrollReview(ReviewCreateRequest reviewCreateRequest) throws Exception {
        Optional<Member> buyer = memberRepository.findById(reviewCreateRequest.getBuyerId());
        Optional<Product> product = productRepository.findById(reviewCreateRequest.getProductId());

        if(buyer.isEmpty()) return RsData.of("500", "존재하지 않는 회원입니다.");
        else if(product.isEmpty()) return RsData.of("500", "존재하지 않는 상품입니다.");

        Optional<Review> targetReview = reviewRepository.findByBuyerAndSellerIdAndProduct(buyer.get(), reviewCreateRequest.getSellerId(), product.get());
        if(targetReview.isPresent()) return RsData.of("500", "이미 판매자 리뷰를 작성했습니다!");

        Review review = Review.builder()
                .buyer(buyer.get())
                .sellerId(reviewCreateRequest.getSellerId())
                .product(product.get())
                .content(reviewCreateRequest.getContent())
                .rating(reviewCreateRequest.getRating())
                .build();
        reviewRepository.save(review);
        return RsData.of("200", "리뷰가 작성되었습니다!", new ReviewDto(review));
    }

    public RsData<Page<ReviewDetailDto>> selectReviewListWithPageable(Long sellerId, Pageable pageable) throws Exception {
        Page<ReviewDetailDto> list = reviewRepository.findAllBySellerId(sellerId, pageable)
                .map(ReviewDetailDto::new);
        return RsData.of("200", "조회 성공!", list);
    }
}
