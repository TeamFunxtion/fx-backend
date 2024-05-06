package com.fx.funxtion.domain.product.controller;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ApiV1ProductController {

    private final ProductService productService;

    @PostMapping("")
    public RsData<ProductDto> register(@RequestBody ProductDto productDto) {
        System.out.println(productDto);

        ProductDto registerDto = productService.registerProduct(productDto);

        // todo. 파일 업로드...

        return RsData.of("200", "상품이 등록되었습니다!", registerDto);
    }
}
