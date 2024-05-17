package com.fx.funxtion;

import com.fx.funxtion.domain.payment.dto.PaymentDto;
import com.fx.funxtion.domain.payment.service.PaymentService;
import com.fx.funxtion.global.RsData.RsData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PaymentTest {

    @Autowired
    private PaymentService paymentService;


    @Test
    public void 결제_내역_조회() {

        RsData<List<PaymentDto>> rs = paymentService.selectPaymentListByUserId("seabongsae@naver.com");

        for(PaymentDto dto: rs.getData()) {
            System.out.println(dto);
        }
    }
}
