package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private MemberDto creator;
    private String categoryId;
    private String productTitle;
    private String productDesc;
    private Long productPrice;
    private String salesTypeId;
    private String qualityTypeId;
    private String statusTypeId;
    private String location;
    private Long currentPrice;
    private Long coolPrice;
    private LocalDateTime endTime;
    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<ProductImageDto> images = new ArrayList<>();
    private int bidCount;

    public ProductDto(Product p) {
        BeanUtils.copyProperties(p, this);
        this.creator = new MemberDto(p.getMember());
        this.bidCount = p.getBids().size();
    }
}
