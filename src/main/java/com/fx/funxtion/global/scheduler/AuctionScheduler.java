package com.fx.funxtion.global.scheduler;

import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.notification.dto.NotificationMessage;
import com.fx.funxtion.domain.notification.service.NotificationService;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Bid;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.entity.ProductStatusType;
import com.fx.funxtion.domain.product.repository.BidRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.domain.payments.safepayment.entity.SafePaymentStatus;
import com.fx.funxtion.domain.payments.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.payments.safepayment.repository.SafePaymentsRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final SafePaymentsRepository safePaymentsRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Scheduled(cron = "0 * * * * ?") // 1분마다
//    @Scheduled(cron = "0 0/5 * * * ?") // 5분마다
    @Transactional
    public void run() {
        try {
            List<Product> list = productRepository.findAllAfterAuctionEndTime();
            for(Product product: list) {
                product.setStatusTypeId(product.getAuctionWinnerId() != null ? ProductStatusType.ST04.name() : ProductStatusType.ST03.name()); // 거래중 또는 판매대기로 변경
                productRepository.save(product);
                if(product.getAuctionWinnerId() != null) {
                    Optional<Member> winner = memberRepository.findById(product.getAuctionWinnerId());
                    if(winner.isPresent()) { // 낙찰자가 존재할 때
                        // 채팅방 생성
                        ChatRoom chatRoom = chatRoomRepository.findByCustomerIdAndMemberId(winner.get().getId(), product.getMember().getId());

                        boolean existSafePaymentsSP03 = false;
                        // 채팅방이 없을땐 ok
                        if (chatRoom == null) {
                            chatRoom = ChatRoom.builder()
                                    .customer(winner.get())
                                    .member(product.getMember())
                                    .product(product)
                                    .build();
                            chatRoomRepository.save(chatRoom);

                        } else { // 판매자와의 채팅방이 존재 && SP03 상황이 존재할때
                            SafePayments safePayments = safePaymentsRepository.findBySellerIdAndBuyerIdAndStatus(product.getMember().getId(), winner.get().getId(), SafePaymentStatus.SP03);
                            existSafePaymentsSP03 = safePayments != null;

                            if (!existSafePaymentsSP03) { // 현재 안전결제 최종 진행중인 내역이 없으면
                                chatRoom.setProduct(product);
                                chatRoomRepository.save(chatRoom);
                            }
                        }

                        // 안전결제 생성 (구매자가 결제완료한 상태로 초기화 -> SP03)
                        SafePayments newSafePayment = SafePayments.builder()
                                .productId(product.getId())
                                .buyerId(winner.get().getId())
                                .sellerId(product.getMember().getId())
                                .status(existSafePaymentsSP03 ? SafePaymentStatus.SP00 : SafePaymentStatus.SP03) // SP00: 결제대기(STOP), SP03(거래중)
                                .sellerOk("N")
                                .buyerOk("N")
                                .build();
                        safePaymentsRepository.save(newSafePayment);

                        // todo. 블라인드 경매일때는 낙찰자를 제외한 모든 입찰 포인트 반환해줘야함
                        if (product.getSalesTypeId().equals("SA02")) { // 블라인드 경매
                            List<Bid> bids = bidRepository.findAllByProductId(product.getId());

                            for (Bid bid : bids) { // 낙찰되지 않은 입찰 포인트 반환하기
                                if (!(bid.getMember().getId().equals(product.getAuctionWinnerId())
                                        && Objects.equals(bid.getBidPrice(), product.getCurrentPrice()))) {
                                    Member member = bid.getMember();
                                    member.setPoint(member.getPoint() + bid.getBidPrice().intValue());
                                    memberRepository.save(member);

                                    bid.setReturnYn("Y");
                                    bidRepository.save(bid);
                                }
                            }
                        }

                        // todo. 낙찰 실패자들한테 알림 전송하기
                        Optional<List<Bid>> bidLosers = bidRepository.findDistinctByProductAndMemberNot(product, winner.get());
                        Set<Long> bidderIds = new HashSet<>();
                        if (!bidLosers.isEmpty()) {
                            for (Bid bid : bidLosers.get()) {
                                if (bidderIds.contains(bid.getMember().getId())) {
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
                        String message = winner.get().getNickname() + "님 축하해요! 상품이 낙찰되었습니다!";
                        NotificationMessage notificationMessage = NotificationMessage.builder()
                                .type("auction_winner")
                                .message(message)
                                .data(new ProductDto(product))
                                .build();
                        // Client에 알림 전송
                        notificationService.notifyUser(winner.get().getId().toString(), notificationMessage);
                        notificationService.createNotification(winner.get().getId(), product.getId(), message);

                        // todo. 판매자에게 낙찰 알림 전송
                        message = product.getMember().getNickname() + "님, 경매상품 낙찰자가 정해졌습니다! 1:1채팅을 통해서 거래를 진행하세요!";
                        notificationService.notifyUser(product.getMember().getId().toString(), NotificationMessage.builder()
                                .type("auction_winner")
                                .message(message)
                                .data(new ProductDto(product))
                                .build());
                        notificationService.createNotification(product.getMember().getId(), product.getId(), message);

                    } else { // 낙찰자가 없을때
                        // todo. 판매자에게 경매종료 알림 전송
                        String message = product.getMember().getNickname() + "님, 아쉽게도 경매 상품의 낙찰자가 없습니다..!";
                        notificationService.notifyUser(product.getMember().getId().toString(), NotificationMessage.builder()
                                .type("auction_nowinner")
                                .message(message)
                                .data(new ProductDto(product))
                                .build());
                        notificationService.createNotification(product.getMember().getId(), product.getId(), message);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("* Batch 시스템이 예기치 않게 종료되었습니다.");
            e.printStackTrace();
        }
    }
}
