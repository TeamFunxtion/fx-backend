package com.fx.funxtion.domain.payment.controller;

import com.fx.funxtion.domain.payment.dto.PaymentDto;
import com.fx.funxtion.domain.payment.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class ApiV1PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/verify/{imp_uid}")
    public PaymentDto verifyPaymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        return paymentService.verifyPayment(imp_uid);
    }
}
