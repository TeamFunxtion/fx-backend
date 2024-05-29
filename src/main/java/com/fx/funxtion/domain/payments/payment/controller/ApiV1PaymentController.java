package com.fx.funxtion.domain.payments.payment.controller;

import com.fx.funxtion.domain.payments.payment.dto.PaymentDto;
import com.fx.funxtion.domain.payments.payment.service.PaymentService;
import com.fx.funxtion.global.RsData.RsData;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class ApiV1PaymentController {
    private final PaymentService paymentService;

    /**
     * 결제 검증
     * @param imp_uid
     * @return PaymentDto
     */
    @GetMapping("/verify/{imp_uid}")
    public PaymentDto verifyPaymentByImpUid(@PathVariable("imp_uid") String imp_uid) {
        PaymentDto paymentDto = null;
        try {
            paymentDto = paymentService.verifyPayment(imp_uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentDto;
    }

    /**
     * 결제 내역 조회
     * @param email
     * @return RsData<List<PaymentDto>>
     */
    @GetMapping("")
    public RsData<List<PaymentDto>> selectPaymentListByUserId(@RequestParam(name="email") String email) {
        try {
            RsData<List<PaymentDto>> listRs = paymentService.selectPaymentListByUserId(email);
            return RsData.of(listRs.getResultCode(), listRs.getMsg(), listRs.getData());
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
