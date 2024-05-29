package com.fx.funxtion.domain.payments.safepayment.controller;

import com.fx.funxtion.domain.payments.safepayment.dto.SafePaymentsDeleteRequest;
import com.fx.funxtion.domain.payments.safepayment.dto.SafePaymentsUpdateRequest;
import com.fx.funxtion.domain.payments.safepayment.service.SafePaymentsService;
import com.fx.funxtion.domain.payments.safepayment.dto.SafePaymentsDetailResponse;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/safe")
@RequiredArgsConstructor
@Slf4j
public class ApiV1safePaymentController {
    private final SafePaymentsService safePaymentsService;

    /**
     * 안전거래 시작 여부 조회
     * @param productId
     * @param sellerId
     * @param buyerId
     * @return RsData<SafePaymentsDetailResponse>
     */
    @GetMapping("")   // pathVariable
    public RsData<SafePaymentsDetailResponse> findSafePayments(@RequestParam("productId") String productId, @RequestParam("sellerId") String sellerId, @RequestParam("buyerId") String buyerId){
        try {
            Long productNo = Long.parseLong(productId);
            Long sellerNo = Long.parseLong(sellerId);
            Long buyerNo = Long.parseLong(buyerId);
            SafePaymentsDetailResponse safePaymentsDetailResponse = safePaymentsService.findSafePayments(productNo, sellerNo, buyerNo);
            return RsData.of("200", "안전거래 시작 여부 조회 성공!", safePaymentsDetailResponse);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 안전거래 결제 완료
     * @param safePaymentsUpdateRequest
     * @return RsData<String>
     */
    @PatchMapping("")
    public RsData<String> updateSafePayment(@RequestBody SafePaymentsUpdateRequest safePaymentsUpdateRequest) {
        try {
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
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }

    /**
     * 안전거래 삭제
     * @param safePaymentsDeleteRequest
     * @return RsData<String>
     */
    @PostMapping("/delete")
    public RsData<String> deleteSafePayment(@RequestBody SafePaymentsDeleteRequest safePaymentsDeleteRequest) {
        try {
            safePaymentsService.deleteSafePayment(safePaymentsDeleteRequest);
            return RsData.of("200", "안전거래 삭제 성공!", "삭제 성공!");
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
