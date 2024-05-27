package com.fx.funxtion.domain.transactionhistory.service;


import com.fx.funxtion.domain.transactionhistory.dto.BuySelectResponse;

import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.safepayment.repository.SafePaymentsRepository;
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
public class BuyService {

    private final ProductService productService;
    private final SafePaymentsRepository safePaymentsRepository;
    private final ProductRepository productRepository;

    public RsData<List<BuySelectResponse>> selectBuyList(Long buyerId){

        List<SafePayments>safePayments  = safePaymentsRepository.findByBuyerId(buyerId);

        List<BuySelectResponse> buyDtos = new ArrayList<>();


       for(SafePayments payments : safePayments) {
          Optional<Product> productOptional = productRepository.findById(payments.getProductId());

           if (productOptional.isPresent()) {
               Product product = productOptional.get();
               System.out.println(productOptional);
               BuySelectResponse response = new BuySelectResponse(
                       payments.getId(),
                       payments.getBuyerId(),
                       product.getProductTitle(),
                       product.getCurrentPrice(),
                       product.getMember().getNickname(),
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
