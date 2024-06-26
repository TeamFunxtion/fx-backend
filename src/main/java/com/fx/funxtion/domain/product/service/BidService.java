package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
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
import com.fx.funxtion.domain.payments.safepayment.entity.SafePaymentStatus;
import com.fx.funxtion.domain.payments.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.payments.safepayment.repository.SafePaymentsRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BidService {
    private final NotificationService notificationService;
    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SafePaymentsRepository safePaymentsRepository;

    @Transactional
    public RsData<BidCreateResponse> createBid(BidCreateRequest bidCreateRequest) throws Exception {
        Product product = productRepository.findById(bidCreateRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        
        Member member = memberRepository.findById(bidCreateRequest.getBidderId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if(member.getPoint() - bidCreateRequest.getBidPrice().intValue() <= 0) {
            return RsData.of("500", "포인트 부족으로 입찰할 수 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        if(now.isEqual(product.getEndTime()) || now.isAfter(product.getEndTime())) {
            throw new IllegalArgumentException("경매가 종료되어 입찰이 불가합니다!");
        }

        boolean isCoolEnded = false; // 즉시구매로 경매종료 여부
        boolean isFirst = product.getBids().isEmpty(); // 최초 입찰 여부
        int bidPrice = bidCreateRequest.getBidPrice().intValue();

        // 오픈 경매일때
        if(product.getSalesTypeId().equals("SA01")) {
            if((isFirst && product.getCurrentPrice() > bidCreateRequest.getBidPrice())
               || (!isFirst && product.getCurrentPrice() >= bidCreateRequest.getBidPrice())) {
                return RsData.of("500", "입찰할 수 없는 금액입니다!");
            }

            if(product.getCoolPrice() != null && product.getCoolPrice() <= bidPrice) { // 입찰가 >= 즉구가
                isCoolEnded = true; // 즉시구매
                bidPrice = product.getCoolPrice().intValue();
            } else if(bidCreateRequest.getBidderId().equals(product.getAuctionWinnerId())) { // 연속 입찰시 불가
                return RsData.of("500", "현재 최고 금액의 입찰자입니다!");
            }

            // todo. 기존 낙찰자 포인트 환불해주기
            if(product.getAuctionWinnerId() != null) {
                Member oldWinner = memberRepository.findById(product.getAuctionWinnerId())
                        .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
                oldWinner.setPoint(oldWinner.getPoint() + product.getCurrentPrice().intValue());

                // 바로 구매가 아닐때 기존 낙찰자에게 알림 전송
                if(!isCoolEnded && !Objects.equals(oldWinner.getId(), bidCreateRequest.getBidderId())) { // 기존 낙찰자랑 입찰자가 다를때
                    String message = oldWinner.getNickname() + "님 다른 구매자에게 낙찰 기회를 뺏겼습니다!";
                    NotificationMessage notificationMessage = NotificationMessage.builder()
                            .type("auction_miss")
                            .message(message)
                            .data(new ProductDto(product))
                            .build();

                    notificationService.notifyUser(oldWinner.getId().toString(), notificationMessage);

                    // 알림 내역 저장
                    notificationService.createNotification(oldWinner.getId(), product.getId(), message);
                }
            }
            product.setCurrentPrice(bidCreateRequest.getBidPrice()); // 현재가 갱신
            product.setAuctionWinnerId(bidCreateRequest.getBidderId()); // 낙찰자 갱신

        } else { // 블라인드 경매일 때
            if((isFirst && product.getProductPrice() > bidCreateRequest.getBidPrice())
                    || (!isFirst && product.getProductPrice() >= bidCreateRequest.getBidPrice())) {
                return RsData.of("500", "입찰할 수 없는 금액입니다!");
            }

            if(bidRepository.findByProductAndMemberAndBidPrice(product, member, bidCreateRequest.getBidPrice()).isPresent()) {
                throw new IllegalArgumentException("같은 입찰가로 입찰한 내역이 존재합니다!");
            }

            Optional<Bid> maxPriceBid = bidRepository.findByProductAndMemberAndBidPriceGreaterThan(product, member, bidCreateRequest.getBidPrice());
            if(maxPriceBid.isPresent()) {
                throw new IllegalArgumentException("이미 " + maxPriceBid.get().getBidPrice() + "원으로 입찰한 내역이 존재합니다!");
            }

            if((!isFirst && bidCreateRequest.getBidPrice() > product.getCurrentPrice()) // 최초 입찰이 아닐때는 >
                || (isFirst && bidCreateRequest.getBidPrice() >= product.getCurrentPrice()) // 최초 입찰시에는 >=
            ) { // 입찰자가 현재가보다 클 때만 낙찰자를 갱신
                product.setCurrentPrice(bidCreateRequest.getBidPrice()); // 현재가 갱신
                product.setAuctionWinnerId(bidCreateRequest.getBidderId()); // 낙찰자 갱신
            }
        }

        member.setPoint(member.getPoint() - bidPrice); // 입찰자 포인트 차감

        if(isCoolEnded) { // 즉시구매시 경매종료(거래중으로 상태변경)
            product.setStatusTypeId(ProductStatusType.ST04.name());
            
            // 채팅방 생성 & 안전결제 SP03 생성
            ChatRoom chatRoom = chatRoomRepository.findByCustomerIdAndMemberId(bidCreateRequest.getBidderId(), product.getMember().getId());

            boolean existSafePaymentsSP03 = false;
            // 채팅방이 없을땐 ok
            if(chatRoom == null) {
                chatRoom = ChatRoom.builder()
                        .customer(member)
                        .member(product.getMember())
                        .product(product)
                        .build();
                chatRoomRepository.save(chatRoom);

            } else { // 판매자와의 채팅방이 존재 && SP03 상황이 존재할때
                SafePayments safePayments = safePaymentsRepository.findBySellerIdAndBuyerIdAndStatus(product.getMember().getId(), bidCreateRequest.getBidderId(), SafePaymentStatus.SP03);
                existSafePaymentsSP03 = safePayments != null;

                if(!existSafePaymentsSP03) { // 현재 안전결제 최종 진행중인 내역이 없으면
                    chatRoom.setProduct(product);
                    chatRoomRepository.save(chatRoom);
                }
            }

            // 안전결제 생성 (구매자가 결제완료한 상태로 초기화 -> SP03)
            SafePayments newSafePayment = SafePayments.builder()
                    .productId(product.getId())
                    .buyerId(bidCreateRequest.getBidderId())
                    .sellerId(product.getMember().getId())
                    .status(existSafePaymentsSP03 ? SafePaymentStatus.SP00 : SafePaymentStatus.SP03) // SP00: 결제대기(STOP), SP03(거래중)
                    .sellerOk("N")
                    .buyerOk("N")
                    .build();
            safePaymentsRepository.save(newSafePayment);

            // todo. 낙찰 실패자들한테 알림 전송하기
            Optional<List<Bid>> bidLosers = bidRepository.findDistinctByProductAndMemberNot(product, member);
            Set<Long> bidderIds = new HashSet<>();
            if(!bidLosers.isEmpty()) {
                for(Bid bid: bidLosers.get()) {
                    if(bidderIds.contains(bid.getMember().getId())) {
                        continue;
                    } else {
                        bidderIds.add(bid.getMember().getId());
                    }
                    String newMessage = "이런! " + bid.getMember().getNickname() + "님, 경매 낙찰에 실패했네요..!";
                    NotificationMessage notificationMessage = NotificationMessage.builder()
                            .type("auction_miss")
                            .message(newMessage)
                            .data(new ProductDto(product))
                            .build();
                    notificationService.notifyUser(bid.getMember().getId().toString(), notificationMessage);
                    notificationService.createNotification(bid.getMember().getId(), product.getId(), newMessage);
                }
            }

            // SSE로 낙찰 알림 전송
            String message = member.getNickname() + "님 축하해요! 상품이 낙찰되었습니다!";
            notificationService.createNotification(member.getId(), product.getId(), message);

            // todo. 판매자에게 낙찰 알림 전송
            message = product.getMember().getNickname() + "님, 경매상품 낙찰자가 정해졌습니다! 1:1채팅을 통해서 거래를 진행하세요!";
            notificationService.notifyUser(product.getMember().getId().toString(), NotificationMessage.builder()
                    .type("auction_winner")
                    .message(message)
                    .data(new ProductDto(product))
                    .build());
            notificationService.createNotification(product.getMember().getId(), product.getId(), message);
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
