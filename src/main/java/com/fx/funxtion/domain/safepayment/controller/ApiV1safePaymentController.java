package com.fx.funxtion.domain.safepayment.controller;

import com.fx.funxtion.domain.chat.dto.ChatMessageDto;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsDetailResponse;
import com.fx.funxtion.domain.safepayment.service.SafePaymentsService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/safe")
@RequiredArgsConstructor
@Slf4j
public class ApiV1safePaymentController {
    private final SafePaymentsService safePaymentsService;

    // 안전거래 시작 여부 조회
    @GetMapping("")   // pathVariable
    public RsData<SafePaymentsDetailResponse> findSafePayments(@RequestParam("productId") String productId, @RequestParam("sellerId") String sellerId, @RequestParam("buyerId") String buyerId){
        Long productNo = Long.parseLong(productId);
        Long sellerNo = Long.parseLong(sellerId);
        Long buyerNo = Long.parseLong(buyerId);
        SafePaymentsDetailResponse safePaymentsDetailResponse = safePaymentsService.findSafePayments(productNo, sellerNo, buyerNo);

        return RsData.of("200", "안전거래 시작 여부 조회 성공!", safePaymentsDetailResponse);
    }
}
