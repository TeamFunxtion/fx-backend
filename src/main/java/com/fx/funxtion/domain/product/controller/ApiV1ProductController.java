package com.fx.funxtion.domain.product.controller;

import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param productCreateRequest
     * @return RsData<ProductCreateResponse>
     */
    @PostMapping("")
    public RsData<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        System.out.println(productCreateRequest);

        RsData<ProductCreateResponse> productCreateResponse = productService.createProduct(productCreateRequest);

        // todo. 파일 업로드...

        return RsData.of(productCreateResponse.getResultCode(), productCreateResponse.getMsg(), productCreateResponse.getData());
    }

    /**
     * 상품 목록 조회
     *
     * @param
     * @return RsData<ProductListResponse>
     */
    @GetMapping("")
    public RsData<List<ProductDto>> getProductList() {
        RsData<List<ProductDto>> response = productService.getProductList();
        return RsData.of(response.getResultCode(), response.getMsg(), response.getData());
    }

    /**
     * 상품 상세 정보 조회
     *  
     * @param id
     * @return RsData<ProductDetailResponse>
     */
    @GetMapping("/{id}")
    public RsData<ProductDetailResponse> getProductDetail(@PathVariable(name="id") Long id) {
        RsData<ProductDetailResponse> productDetailResponse = productService.getProductDetail(id);
        productService.updateViews(id); // 조회수 증가

        return RsData.of(productDetailResponse.getResultCode(), productDetailResponse.getMsg(), productDetailResponse.getData());
    }

    /**
     * 상품 정보 수정 (기본정보 / 상태변경 )
     *
     * @param productUpdateRequest
     * @return RsData<ProductUpdateResponse>
     */
    @PatchMapping("/{id}")
    public RsData<ProductUpdateResponse> updateProduct(@PathVariable(name="id") Long id, @RequestBody ProductUpdateRequest productUpdateRequest) {
        System.out.println(productUpdateRequest);

        RsData<ProductUpdateResponse> productUpdateResponse = productService.updateProduct(id, productUpdateRequest);

        return RsData.of(productUpdateResponse.getResultCode(), productUpdateResponse.getMsg(), productUpdateResponse.getData());
    }
}
