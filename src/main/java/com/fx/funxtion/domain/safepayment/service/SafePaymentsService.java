package com.fx.funxtion.domain.safepayment.service;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsDetailResponse;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.safepayment.repository.SafePaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SafePaymentsService {

    private final SafePaymentsRepository safePaymentsRepository;

    public SafePaymentsDetailResponse findSafePayments(Long productId, Long sellerId, Long buyerId) {
        SafePayments sp = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(productId, sellerId, buyerId);
        SafePaymentsDetailResponse spdr = new SafePaymentsDetailResponse(sp.getSellerOk(), sp.getBuyerOk(), sp.getStartYn(), sp.getEndYn());
        return spdr;
    }
}
