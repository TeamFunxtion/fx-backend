package com.fx.funxtion.domain.safepayment.controller;

import com.fx.funxtion.domain.chat.dto.ChatMessageDto;
import com.fx.funxtion.domain.chat.dto.ChatMessageUpdateRequest;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsDetailResponse;
import com.fx.funxtion.domain.safepayment.dto.SafePaymentsUpdateRequest;
import com.fx.funxtion.domain.safepayment.service.SafePaymentsService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    // 안전거래 결제 완료
    @PatchMapping("")
    public RsData<String> updateSafePayment(@RequestBody SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
        if(safePaymentsUpdateRequest.getStatus().equals("buyerPayment")) {
            safePaymentsService.updateSafePayment(safePaymentsUpdateRequest);
            return RsData.of("200", "안전거래 결제 성공!", "결제 성공!");
        } else if(safePaymentsUpdateRequest.getStatus().equals("sellerOk")) {
            safePaymentsService.updateSellerOk(safePaymentsUpdateRequest);
            return RsData.of("200", "안전거래 판매 확정!", "안전거래 판매 확정!");
        } else if(safePaymentsUpdateRequest.getStatus().equals("buyerOk")) {
            safePaymentsService.updateBuyerOk(safePaymentsUpdateRequest);
            return RsData.of("200", "안전거래 구매 확정!", "안전거래 구매 확정!");
        } else {
            return null;
        }


    }

//    // 안전거래 판매 확정
//    @PatchMapping("/seller")
//    public RsData<String> updateSellerOk(@RequestBody SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
//
//
//    }
//
//    // 안전거래 구매 확정
//    @PatchMapping("/buyer")
//    public RsData<String> updateBuyerOk(@RequestBody SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
//
//
//    }

}
