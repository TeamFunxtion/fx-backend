package com.fx.funxtion.domain.transactionhistory.dto;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SellDto {

    private Long id;
    private Long BuyerId;
    private Long ProductId;
    private ProductDto product;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public SellDto(SafePayments safePayments){
        BeanUtils.copyProperties(safePayments, this);

    }



}
