package com.fx.funxtion.domain.safepayment.service;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatRoomRepository;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsDetailResponse;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsUpdateRequest;
import com.fx.funxtion.domain.safepayment.entity.SafePaymentStatus;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.safepayment.repository.SafePaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SafePaymentsService {

    private final SafePaymentsRepository safePaymentsRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ChatRoomRepository chatRoomRepository;

    public SafePaymentsDetailResponse findSafePayments(Long productId, Long sellerId, Long buyerId) {
        SafePayments sp = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(productId, sellerId, buyerId);

        SafePaymentsDetailResponse spdr = null;
        if (sp != null) {
            spdr = new SafePaymentsDetailResponse(sp.getSellerOk(), sp.getBuyerOk(), sp.getStatus().name());
        }
        return spdr;
    }

    // 구매자가 결제를 하는 경우
    public void updateSafePayment(SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
        SafePayments sp = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(safePaymentsUpdateRequest.getProductId(), safePaymentsUpdateRequest.getSellerId(), safePaymentsUpdateRequest.getBuyerId());
        if(sp != null) {
            sp.setStatus(SafePaymentStatus.SP03);
            safePaymentsRepository.save(sp);
        }
        Optional<Product> product = productRepository.findById(safePaymentsUpdateRequest.getProductId());
        Long price = 0L;
        if(product != null) {
            price = product.get().getCurrentPrice();
            if(sp.getStatus().equals(SafePaymentStatus.SP03)) {
                product.get().setStatusTypeId("ST04");
                productRepository.save(product.get());
            }
        }
        int productPrice = price.intValue();
        Optional<Member> m = memberRepository.findById(safePaymentsUpdateRequest.getBuyerId());
        Member member = m.get();
        if(member != null && member.getPoint() >= productPrice) {
            member.setPoint(member.getPoint() - productPrice);
            memberRepository.save(member);
        }
    }

    // 판매자가 판매 확정 누른 경우
    public void updateSellerOk(SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
        SafePayments sp = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(safePaymentsUpdateRequest.getProductId(), safePaymentsUpdateRequest.getSellerId(), safePaymentsUpdateRequest.getBuyerId());
        Optional<Product> p = productRepository.findById(safePaymentsUpdateRequest.getProductId());
        Product product = p.get();
        if(sp != null) {
            sp.setSellerOk("Y");
            if(sp.getBuyerOk()!= null && sp.getBuyerOk().equals("Y")) {
                sp.setStatus(SafePaymentStatus.SP04);
                Optional<Member> member = memberRepository.findById(safePaymentsUpdateRequest.getSellerId());
                Member seller = member.get();
                seller.setPoint(seller.getPoint() + product.getCurrentPrice().intValue());
                memberRepository.save(seller);
            }
            safePaymentsRepository.save(sp);
        }

        if(p != null && sp.getStatus().equals(SafePaymentStatus.SP04)) {
            product.setStatusTypeId("ST02");
            productRepository.save(product);

            SafePayments sp00 = safePaymentsRepository
                    .findFirstBySellerIdAndBuyerIdAndStatusOrderByIdAsc(safePaymentsUpdateRequest.getProductId(),
                                                safePaymentsUpdateRequest.getSellerId(), SafePaymentStatus.SP00);
            if(sp00 != null) {
                sp00.setStatus(SafePaymentStatus.SP03);
                safePaymentsRepository.save(sp00);
                Product pr = productRepository.findById(sp00.getProductId()).get();
                ChatRoom cr = chatRoomRepository.findByCustomerIdAndMemberId(sp00.getBuyerId(), sp00.getSellerId());
                if(cr!= null) {
                    cr.setProduct(pr);
                    chatRoomRepository.save(cr);
                }
            }
        }

    }

    // 구매자가 구매 확정 누른 경우
    public void updateBuyerOk(SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
        SafePayments sp = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(safePaymentsUpdateRequest.getProductId(), safePaymentsUpdateRequest.getSellerId(), safePaymentsUpdateRequest.getBuyerId());
        Optional<Product> p = productRepository.findById(safePaymentsUpdateRequest.getProductId());
        Product product = p.get();
        if(sp != null) {
            sp.setBuyerOk("Y");
            if(sp.getSellerOk()!= null && sp.getSellerOk().equals("Y")) {
                sp.setStatus(SafePaymentStatus.SP04);
                Optional<Member> member = memberRepository.findById(safePaymentsUpdateRequest.getSellerId());
                Member seller = member.get();
                seller.setPoint(seller.getPoint() + product.getCurrentPrice().intValue());
                memberRepository.save(seller);
            }
            safePaymentsRepository.save(sp);
        }

        if(product != null && sp.getStatus().equals(SafePaymentStatus.SP04)) {
            product.setStatusTypeId("ST02");
            productRepository.save(product);

            SafePayments sp00 = safePaymentsRepository
                    .findFirstBySellerIdAndBuyerIdAndStatusOrderByIdAsc(safePaymentsUpdateRequest.getProductId(),
                            safePaymentsUpdateRequest.getSellerId(), SafePaymentStatus.SP00);
            if(sp00 != null) {
                sp00.setStatus(SafePaymentStatus.SP03);
                safePaymentsRepository.save(sp00);
                Product pr = productRepository.findById(sp00.getProductId()).get();
                ChatRoom cr = chatRoomRepository.findByCustomerIdAndMemberId(sp00.getBuyerId(), sp00.getSellerId());
                if(cr!= null) {
                    cr.setProduct(pr);
                    chatRoomRepository.save(cr);
                }
            }
        }
    }
}
