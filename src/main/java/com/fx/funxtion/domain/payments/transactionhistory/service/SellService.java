package com.fx.funxtion.domain.payments.transactionhistory.service;


import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.domain.payments.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.payments.safepayment.repository.SafePaymentsRepository;
import com.fx.funxtion.domain.payments.transactionhistory.dto.SellSelectResponse;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellService {
    private final SafePaymentsRepository safePaymentsRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public RsData<List<SellSelectResponse>> selectSellList(Long sellerId) throws Exception {
        List<SafePayments>safePayments  = safePaymentsRepository.findBySellerId(sellerId);
        List<SellSelectResponse> buyDtos = new ArrayList<>();

        for(SafePayments payments : safePayments) {
          Optional<Product> productOptional = productRepository.findById(payments.getProductId());
          Optional<Member> memberOptional = memberRepository.findById(payments.getBuyerId());
           if (productOptional.isPresent()) {
               Product product = productOptional.get();
               Member member = memberOptional.get();
               SellSelectResponse response = new SellSelectResponse(
                       payments.getId(),
                       product.getProductTitle(),
                       product.getCurrentPrice(),
                       member.getNickname(),
                       product.getSalesTypeId(),
                       payments.getCreateDate(),
                       payments.getUpdateDate()
               );
               buyDtos.add(response);
           }
        }
        return  RsData.of("200", "구매이력 조회 성공!",buyDtos);
    }
}
