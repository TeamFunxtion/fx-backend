package com.fx.funxtion.domain.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.service.BidService;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.domain.product.service.ReportService;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.fx.funxtion.domain.product.service.ProductService.getPageableSort;

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
    private final ReportService reportService;

    /**
     * 상품 등록 with MultipartFiles
     * @param multipartFiles
     * @param newProduct
     * @return RsData<ProductCreateResponse>
     */
    @PostMapping("")
    public RsData<ProductCreateResponse> createProduct(@RequestParam(value="file", required = false) MultipartFile[] multipartFiles, @RequestParam(value="newProduct", required = false) String newProduct) { // 파라미터의 이름은 client의 formData key값과 동일해야함
        RsData<ProductCreateResponse> productCreateResponse;

        try {
            // 객체는 client에서 직렬화를 하여 전달
            ProductCreateRequest productCreateRequest = new ObjectMapper().readValue(newProduct, ProductCreateRequest.class); // String to Object
            System.out.println("productCreateRequest= " + productCreateRequest);

            productCreateResponse = productService.createProduct(productCreateRequest, multipartFiles);
            return RsData.of(productCreateResponse.getResultCode(), productCreateResponse.getMsg(), productCreateResponse.getData());

        } catch(Exception e) {
            return RsData.of("500", "잘못된 Product 정보입력입니다.");
        }
    }

    /**
     * 상품 정보 수정 (기본정보)
     * @param multipartFiles
     * @param newProduct
     * @return RsData<ProductUpdateResponse>
     */
    @PatchMapping("")
    public RsData<ProductUpdateResponse> updateProduct(@RequestParam(value="file", required = false) MultipartFile[] multipartFiles, @RequestParam(value="newProduct", required = false) String newProduct) {
        try {
            // 객체는 client에서 직렬화를 하여 전달
            ProductUpdateRequest productUpdateRequest = new ObjectMapper().readValue(newProduct, ProductUpdateRequest.class); // String to Object
            System.out.println("productUpdateRequest= " + productUpdateRequest);

            RsData<ProductUpdateResponse> productUpdateResponse = productService.updateProduct(productUpdateRequest, multipartFiles);
            return RsData.of(productUpdateResponse.getResultCode(), productUpdateResponse.getMsg(), productUpdateResponse.getData());

        } catch(Exception e) {
            return RsData.of("500", "잘못된 Product 정보입력입니다.");
        }
    }

    /**
     * 상품 정보 수정 (상태변경 )
     *
     * @param productUpdateRequest
     * @return RsData<ProductUpdateResponse>
     */
    @PatchMapping("/status")
    public RsData<ProductUpdateResponse> changeProductStatus(@RequestBody ProductUpdateRequest productUpdateRequest) {
        RsData<ProductUpdateResponse> productUpdateResponse = productService.changeProductStatus(productUpdateRequest);
        return RsData.of(productUpdateResponse.getResultCode(), productUpdateResponse.getMsg(), productUpdateResponse.getData());
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
                                   @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));

        Page<ProductDto> pageList;

        if(category != null && !category.equals("")) {
            pageList = productService.searchByCategory(category, pageable);
        } else {
            pageList = productService.searchByKeyword(keyword, pageable);
        }
        return pageList;
    }

    /**
     * 마이페이지 > 내 상품 목록 조회
     */
    @GetMapping("/my")
    public Page<ProductDto> getMyProducts(@RequestParam(required = true, value = "userId") Long userId,
                                          @RequestParam(required = false, defaultValue = "ST01", value = "status") String statusTypeId,
                                          @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                          @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                          @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));
        Page<ProductDto> pageList = productService.getMyProducts(userId, statusTypeId, pageable);
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

    /**
     * 신고하기
     * @param productReportRequest
     * @return RsData<Long>
     */
    @PostMapping("/reports")
    public RsData<Long> reports(@RequestBody ProductReportRequest productReportRequest) {
        RsData<Long> reportRs = reportService.report(productReportRequest.getUserId(), productReportRequest.getProductId(), productReportRequest.getReportTypeCode());
        return RsData.of(reportRs.getResultCode(), reportRs.getMsg(), reportRs.getData());
    }

    /**
     * 진행중인 경매(판매자)
     * @param
     * @return Page<ProductDto>
     */
    @GetMapping("/my/auction")
    public Page<ProductDto> getAuctionProducts(@RequestParam(required = true, value = "userId") Long userId,
                                               @RequestParam(required = false, defaultValue = "ST01", value = "status") String statusTypeId,
                                               @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                               @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                               @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));
        Page<ProductDto> pageList = productService.getAuctionProducts(userId, statusTypeId, pageable);
        return pageList;
    }

    /**
     * 참여중인 경매(구매자)
     * @param
     * @return Page<ProductDto>
     */
    @GetMapping("/my/bids")
    public Page<ProductDto> getBidProducts(@RequestParam(required = true, value = "userId") Long userId,
                                               @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                               @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                               @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));
        Page<ProductDto> pageList = productService.getBidProducts(userId, pageable);
        return pageList;

    }
}
