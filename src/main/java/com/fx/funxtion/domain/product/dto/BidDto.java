package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.entity.Bid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BidDto {
    private Long id;
    private Long productId;
    private Long bidderId;
    private Long bidPrice;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public BidDto(Bid bid) {
        this.id = bid.getId();
        this.productId = new ProductDto(bid.getProduct()).getId();
        this.bidderId = new MemberDto(bid.getMember()).getId();
        this.bidPrice = bid.getBidPrice();
        this.createDate = bid.getCreateDate();
        this.updateDate = bid.getUpdateDate();
    }
}
