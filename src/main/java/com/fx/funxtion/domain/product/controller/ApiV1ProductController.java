package com.fx.funxtion.domain.product.controller;

import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.service.BidService;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final BidService bidService;

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
    public Page<ProductDto> search(@RequestParam(required = false, defaultValue = "", value = "keyword") String keyword,
                                   @RequestParam(required = false, defaultValue = "", value = "category") String category,
                                   @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                   @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                   @PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        int pageSize = 10;

        Page<ProductDto> pageList;

        if(category != null && !category.equals("")) {
            pageList = productService.searchByCategory(category, pageable, pageNo, pageSize, sort);
        } else {
            pageList = productService.searchByKeyword(keyword, pageable, pageNo, pageSize, sort);
        }
        return pageList;
    }

    /**
     * 상품 상세 정보 조회
     *  
     * @param id
     * @return RsData<ProductDetailResponse>
     */
    @GetMapping("/{id}")
    public RsData<ProductDetailResponse> getProductDetail(@PathVariable(name="id") Long id, @RequestParam(value = "u", defaultValue = "") Long userId) {
        RsData<ProductDetailResponse> productDetailResponse = productService.getProductDetail(id, userId);
        return RsData.of(productDetailResponse.getResultCode(), productDetailResponse.getMsg(), productDetailResponse.getData());
    }

    /**
     * 상품 조회수 증가
     *
     * @param id
     * @return RsData<Void>
     */
    @GetMapping("/{id}/views")
    public RsData<Void> increaseViews(@PathVariable(name="id") Long id) {
        productService.increaseViews(id); // 조회수 증가
        return RsData.of("200", "조회수 증가", null);
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

    /**
     * 경매 입찰하기
     *
     * @param bidCreateRequest
     * @return RsData<BidCreateResponse>
     */
    @PostMapping("/bid")
    public RsData<BidCreateResponse> createBid(@RequestBody BidCreateRequest bidCreateRequest) {
        System.out.println(bidCreateRequest);

        RsData<BidCreateResponse> bidCreateResponseRsData = bidService.createBid(bidCreateRequest);

        return RsData.of(bidCreateResponseRsData.getResultCode(), bidCreateResponseRsData.getMsg(), bidCreateResponseRsData.getData());
    }

    /**
     * 관심상품 등록/해제
     *
     * @param favoriteUpdateRequest
     * @return RsData<Long>
     */
    @PutMapping("/favorite")
    public RsData<String> favorite(@RequestBody FavoriteUpdateRequest favoriteUpdateRequest) {
        boolean result = productService.updateFavorite(favoriteUpdateRequest.getUserId(), favoriteUpdateRequest.getProductId());

        return RsData.of("200", "성공", result ? "Y" : "N");
    }
}
