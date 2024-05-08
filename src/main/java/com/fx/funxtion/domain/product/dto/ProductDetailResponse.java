package com.fx.funxtion.domain.product.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ProductDetailResponse {
    private Long id;
    private MemberDto seller;
    private String categoryId;
    private String productTitle;
    private String productDesc;
    private Long productPrice;
    private String salesTypeId;
    private String qualityTypeId;
    private String statusTypeId;
    private String location;
    private Long coolPrice;
    private Long currentPrice;
    private LocalDateTime endTime;
    private int views;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private List<ProductImageDto> images = new ArrayList<>();
    private List<BidDto> bids = new ArrayList<>();

    public ProductDetailResponse(Product p) {
        BeanUtils.copyProperties(p, this);
        this.seller = new MemberDto(p.getMember());
        this.images = p.getImages().stream().map(ProductImageDto::new).toList();
        this.bids  = p.getBids().stream().map(BidDto::new).toList();
    }
}
