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

    @GetMapping("/verify/{imp_uid}")
    public PaymentDto verifyPaymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        return paymentService.verifyPayment(imp_uid);
    }

    @GetMapping("")
    public RsData<List<PaymentDto>> selectPaymentListByUserId(@RequestParam(name="email") String email) {
        RsData<List<PaymentDto>> listRs = paymentService.selectPaymentListByUserId(email);
        return RsData.of(listRs.getResultCode(), listRs.getMsg(), listRs.getData());


    }
}
