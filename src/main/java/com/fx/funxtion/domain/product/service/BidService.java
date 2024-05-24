package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.notification.dto.NotificationMessage;
import com.fx.funxtion.domain.notification.service.NotificationService;
import com.fx.funxtion.domain.product.dto.BidCreateRequest;
import com.fx.funxtion.domain.product.dto.BidCreateResponse;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Bid;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.BidRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidService {

    private final NotificationService notificationService;
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

        boolean isCoolEnded = false; // 즉시구매로 경매종료 여부
        int bidPrice = bidCreateRequest.getBidPrice().intValue();

        // 오픈 경매일때
        if(product.getSalesTypeId().equals("SA01")) {

            if(product.getCurrentPrice() >= bidCreateRequest.getBidPrice()) {
                throw new IllegalArgumentException("입찰가가 현재가보다 낮습니다.");
            }

            if(product.getCoolPrice() != null && product.getCoolPrice() <= bidPrice) { // 입찰가가 즉구가 보다 클때
                isCoolEnded = true; // 즉시구매
                bidPrice = product.getCoolPrice().intValue();
            }

            // todo. 기존 낙찰자 포인트 환불해주기
            if(product.getAuctionWinnerId() != null) {
                Member oldWinner = memberRepository.findById(product.getAuctionWinnerId())
                        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
                oldWinner.setPoint(oldWinner.getPoint() + product.getCurrentPrice().intValue());

                // 알림 전송
                if(oldWinner.getId() != bidCreateRequest.getBidderId()) { // 기존 낙찰자랑 입찰자가 다를때
                    NotificationMessage notificationMessage = NotificationMessage.builder()
                            .type("auction_miss")
                            .message("이런, 다른 구매자에게 낙찰 기회를 빼앗겼어요..!")
                            .data(new ProductDto(product))
                            .build();

                    notificationService.notifyUser(oldWinner.getId().toString(), notificationMessage);
                }
            }

        } else { // 블라인드 경매일 때
            if(product.getProductPrice() >= bidCreateRequest.getBidPrice()) {
                throw new IllegalArgumentException("입찰가가 시작가보다 낮습니다.");
            }
        }

        if(bidPrice > product.getCurrentPrice()) { // 입찰가가 현재가보다 클때
            // 현재가 및 낙찰자 갱신
            product.setCurrentPrice(bidCreateRequest.getBidPrice());
            product.setAuctionWinnerId(bidCreateRequest.getBidderId());
        }

        // 포인트 차감
        member.setPoint(member.getPoint() - bidPrice);

        if(isCoolEnded) { // 즉시구매시 경매종료(거래중으로 상태변경)
            product.setStatusTypeId(ProductStatusType.ST04.name());
        }


        Bid bid = Bid.builder()
                .product(product)
                .member(member)
                .bidPrice(bidCreateRequest.getBidPrice())
                .returnYn("N")
                .build();

        bidRepository.save(bid);

        if(bid.getId() != null && bid.getId() > 0) {
            return RsData.of("200", !isCoolEnded ? "입찰 등록 성공!" : "바로구매 성공! 판매자와 거래를 진행하시기 바랍니다.", new BidCreateResponse(bid, isCoolEnded));
        } else {
            return RsData.of("500", !isCoolEnded ? "입찰 등록 실패!" : "바로구매 실패!");
        }
    }
}
