package com.fx.funxtion.domain.payments.transactionhistory.controller;


import com.fx.funxtion.domain.payments.transactionhistory.service.SellService;
import com.fx.funxtion.domain.payments.transactionhistory.dto.SellSelectResponse;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sells")
public class ApiV1SellerController {
    private final SellService sellService;

    @GetMapping("")
    public RsData<List<SellSelectResponse>> selectSellList(@RequestParam(name="sellerId") Long sellerId){

        RsData<List<SellSelectResponse>> listRs = sellService.selectSellList(sellerId);
        return RsData.of(listRs.getResultCode(), listRs.getMsg(), listRs.getData());

    }
}
