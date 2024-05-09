package com.fx.funxtion.global.scheduler;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

//@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 * * * * ?") // 1분마다
//    @Scheduled(cron = "0 0/5 * * * ?") // 5분마다
    public void run() {
        try {
            List<ProductDto> list = productRepository.findAllAfterAuctionEndTime()
                    .stream()
                    .map(ProductDto::new)
                    .toList();

            for(ProductDto productDto: list) {
//                System.out.println(productDto);

                Product product = productRepository.findById(productDto.getId())
                        .orElseThrow(()-> new IllegalArgumentException("상품이 존재하지 않습니다."));

                if(product.getAuctionWinnerId() != null) { // 낙찰자가 있을 경우
                    product.setStatusTypeId("ST04"); // 거래중으로 상태 변경
                } else { // 낙찰자가 없으면 판매대기 상태로 변경
                    product.setStatusTypeId("ST03");
                }
                productRepository.save(product);
            }

        } catch (Exception e) {
            System.out.println("* Batch 시스템이 예기치 않게 종료되었습니다.");
        }
    }
}
