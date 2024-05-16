package com.fx.funxtion.domain.payment.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.payment.dto.PaymentDto;
import com.fx.funxtion.domain.payment.entity.PaymentEntity;
import com.fx.funxtion.domain.payment.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class PaymentService {
    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    public PaymentService(PaymentRepository paymentRepository, MemberRepository memberRepository) {
        this.iamportClient = new IamportClient("8773443842444657",
                "4z9Zod44545jqAePeCEOQ3BjmMI5QGhsBKkYSnkrOMQ1kxk8tueDTw0fmuYvxX1GRlrG0svwCh1dO0pq");
        this.paymentRepository = paymentRepository;
        this.memberRepository = memberRepository;
    }

    public PaymentDto verifyPayment(String imp_uid) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid); // 결제 검증

        Long amount = (iamportResponse.getResponse().getAmount()).longValue(); // 결제금액
        String status = iamportResponse.getResponse().getStatus(); // paid 이면 1
        String buyerEmail = iamportResponse.getResponse().getBuyerEmail();

        PaymentDto paymentDto = PaymentDto.builder()
                .impUid(imp_uid)
                .amount(amount)
                .status(status)
                .build();

        if(paymentRepository.countByImpUidContainsIgnoreCase(imp_uid) == 0) { // 중복하는 값이 없으면
            if(iamportResponse.getResponse().getStatus().equals("paid")) { // 결제가 정상적으로 이루어졌으면
                PaymentEntity paymentEntity = new PaymentEntity(paymentDto);
                paymentRepository.save(paymentEntity); // 저장

                Optional<Member> member = memberRepository.findByEmail(buyerEmail);
                if(member.isPresent()) { // 결제금액만큼 포인트 증가
                    Member m = member.get();
                    m.setPoint(m.getPoint() + amount.intValue());
                    memberRepository.save(m);
                }

                return paymentDto;
            } else {
                paymentDto.setStatus("결제 오류입니다. 다시 시도해주세요."); // 클라이언트에 Status 값 오류 코드 보냄
                return paymentDto;
            }
        } else {
            paymentDto.setStatus("이미 결제되었습니다.");
            return paymentDto;
        }
    }
}
