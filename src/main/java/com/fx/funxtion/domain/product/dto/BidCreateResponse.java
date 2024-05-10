package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.product.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidCreateResponse {
    private Long id;
    private Long bidderId;
    private Long productId;
    private Long bidPrice;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public BidCreateResponse(Bid bid){
        BeanUtils.copyProperties(bid, this);
    }
}
