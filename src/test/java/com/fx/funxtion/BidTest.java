package com.fx.funxtion;

import com.fx.funxtion.domain.product.dto.BidCreateRequest;
import com.fx.funxtion.domain.product.dto.BidCreateResponse;
import com.fx.funxtion.domain.product.service.BidService;
import com.fx.funxtion.global.RsData.RsData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BidTest {

    @Autowired
    private BidService bidService;

    @Test
    public void 입찰하기() throws Exception {
        BidCreateRequest bidCreateRequest = BidCreateRequest.builder()
                .productId(4L)
                .bidderId(1L)
                .bidPrice(10000L)
                .build();

        RsData<BidCreateResponse> bidCreateResponse = bidService.createBid(bidCreateRequest);
        assertThat(bidCreateResponse.getData().getId()).isGreaterThan(0L);
    }
}
