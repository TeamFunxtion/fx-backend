package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.dto.BidCreateRequest;
import com.fx.funxtion.domain.product.dto.BidCreateResponse;
import com.fx.funxtion.domain.product.entity.Bid;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.BidRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public RsData<BidCreateResponse> createBid(BidCreateRequest bidCreateRequest) {
        Product product = productRepository.findById(bidCreateRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        
        Member member = memberRepository.findById(bidCreateRequest.getBidderId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        
        Bid bid = Bid.builder()
                .product(product)
                .member(member)
                .bidPrice(bidCreateRequest.getBidPrice())
                .returnYn("N")
                .build();

        bidRepository.save(bid);

        // todo. 현재가 갱신되었으니 이전 최고 입찰자 환불시키기?

        if(bid.getId() != null && bid.getId() > 0) {
            return RsData.of("200", "입찰 등록 성공!", new BidCreateResponse(bid));
        } else {
            return RsData.of("500", "입찰 등록 실패!");
        }
    }
}
