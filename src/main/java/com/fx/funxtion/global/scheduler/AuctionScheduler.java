package com.fx.funxtion.global.scheduler;

import com.fx.funxtion.domain.product.dto.BidDto;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.BidRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Objects;

//@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final ProductRepository productRepository;
    private final BidRepository bidRepository;

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

                // todo. 블라인드 경매일때는 낙찰자를 제외한 모든 입찰 포인트 반환해줘야함
//                if(productDto.getSalesTypeId().equals("SA02")) {
//                    List<BidDto> bidDtos = bidRepository.findAllByProductId(productDto.getId())
//                            .stream()
//                            .map(BidDto::new)
//                            .toList();
//
//                    for(BidDto bidDto: bidDtos) { // 낙찰되지 않은 입찰 포인트 반환하기
//                        if(!(bidDto.getBidderId().equals(product.getAuctionWinnerId()) && Objects.equals(bidDto.getBidPrice(), productDto.getCurrentPrice()))) {
//                        }
//                    }
//                }

            }

        } catch (Exception e) {
            System.out.println("* Batch 시스템이 예기치 않게 종료되었습니다.");
        }
    }
}
