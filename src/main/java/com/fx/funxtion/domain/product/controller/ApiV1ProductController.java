package com.fx.funxtion.domain.product.controller;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 관련 Controller
 * 상품 조회, 등록, 수정(or 상태 변경)
 *
 * @author Kim Geon Wee
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ApiV1ProductController {

    private final ProductService productService;

    /**
     * 상품 등록
     *
     * @param productDto
     * @return RsData<ProductDto>
     */
    @PostMapping("")
    public RsData<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        System.out.println(productDto);

        RsData<ProductDto> registerRs = productService.createProduct(productDto);

        // todo. 파일 업로드...

        return RsData.of(registerRs.getResultCode(), registerRs.getMsg(), registerRs.getData());
    }

    /**
     * 상품 상세 정보 조회
     *  
     * @param id
     * @return RsData<ProductDto>
     */
    @GetMapping("/{id}")
    public RsData<ProductDto> getProduct(@PathVariable(name="id") Long id) {
        System.out.println(id);

        RsData<ProductDto> getProductRs = productService.getProduct(id);

        return RsData.of(getProductRs.getResultCode(), getProductRs.getMsg(), getProductRs.getData());
    }

    /**
     * 상품 정보 수정 (기본정보 / 상태변경 )
     *
     * @param productDto
     * @return RsData<ProductDto>
     */
    @PatchMapping("/{id}")
    public RsData<ProductDto> updateProduct(@PathVariable(name="id") Long id, @RequestBody ProductDto productDto) {
        System.out.println(productDto);

        RsData<ProductDto> editProductRs = productService.updateProduct(id, productDto);

        return RsData.of(editProductRs.getResultCode(), editProductRs.getMsg(), editProductRs.getData());
    }
}
