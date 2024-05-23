package com.fx.funxtion.domain.transactionhistory.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellSelectResponse {

    private Long id;
    private String productTitle;
    private Long productPrice;
    private String nickname;
    private String salesTypeId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


}