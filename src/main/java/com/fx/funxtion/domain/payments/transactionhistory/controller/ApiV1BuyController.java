package com.fx.funxtion.domain.payments.transactionhistory.controller;


import com.fx.funxtion.domain.payments.transactionhistory.service.BuyService;
import com.fx.funxtion.domain.payments.transactionhistory.dto.BuySelectResponse;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buys")
public class ApiV1BuyController {
    private final BuyService buyService;

    @GetMapping("")
    public RsData<List<BuySelectResponse>> selectBuyList(@RequestParam(name="buyerId") Long buyerId){

        RsData<List<BuySelectResponse>> listRs = buyService.selectBuyList(buyerId);
        return RsData.of(listRs.getResultCode(), listRs.getMsg(), listRs.getData());

    }
}
