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

        if(member.getPoint() - bidCreateRequest.getBidPrice().intValue() <= 0) {
            throw new IllegalArgumentException("포인트 부족으로 입찰할 수 없습니다.");
        }

        // 오픈 경매일때
        if(product.getSalesTypeId().equals("SA01")) {
            if(product.getCurrentPrice() >= bidCreateRequest.getBidPrice()) {
                throw new IllegalArgumentException("입찰가가 현재가보다 낮습니다.");
            }

            // todo. 기존 낙찰자 포인트 환불해주기
            if(product.getAuctionWinnerId() != null) {
                Member oldWinner = memberRepository.findById(product.getAuctionWinnerId())
                        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
                oldWinner.setPoint(oldWinner.getPoint() + product.getCurrentPrice().intValue());
            }

        } else { // 블라인드 경매일 때
            if(product.getProductPrice() >= bidCreateRequest.getBidPrice()) {
                throw new IllegalArgumentException("입찰가가 시작가보다 낮습니다.");
            }
        }

        // 현재가 및 낙찰자 갱신
        product.setCurrentPrice(bidCreateRequest.getBidPrice());
        product.setAuctionWinnerId(bidCreateRequest.getBidderId());

        // 포인트 차감
        member.setPoint(member.getPoint() - bidCreateRequest.getBidPrice().intValue());

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
