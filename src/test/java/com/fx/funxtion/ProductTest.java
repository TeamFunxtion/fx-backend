package com.fx.funxtion;

import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import java.util.List;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductService productService;

    @Test
    public void 상품_데이터_등록_오픈형경매() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setStoreId(1L);
        productCreateRequest.setCategoryId("CA01");
        productCreateRequest.setProductTitle("[오픈형경매]상품타이틀");
        productCreateRequest.setProductDesc("상품에 대한 설명입니다.");
        productCreateRequest.setProductPrice(13000L);
        productCreateRequest.setQualityTypeId("QU01");
        productCreateRequest.setSalesTypeId("SA01");
        productCreateRequest.setLocation("서울시 강남구");
        productCreateRequest.setCoolPrice(20000L);
        productCreateRequest.setEndDays(3);

        RsData<ProductCreateResponse> productCreateResponse = productService.createProduct(productCreateRequest, new MultipartFile[0]);

        assertThat(productCreateRequest.getStoreId())
                .isEqualTo(productCreateResponse.getData().getStoreId());
    }

    @Test
    public void 상품_데이터_등록_일반판매() throws Exception {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setStoreId(1L);
        productCreateRequest.setCategoryId("CA01");
        productCreateRequest.setProductTitle("[일반판매]상품타이틀");
        productCreateRequest.setProductDesc("상품에 대한 설명입니다.");
        productCreateRequest.setProductPrice(13000L);
        productCreateRequest.setQualityTypeId("QU01");
        productCreateRequest.setSalesTypeId("SA01");
        productCreateRequest.setLocation("서울시 강남구");

        RsData<ProductCreateResponse> productCreateResponse = productService.createProduct(productCreateRequest, new MultipartFile[0]);

        assertThat(productCreateRequest.getStoreId())
                .isEqualTo(productCreateResponse.getData().getStoreId());
    }

    @Test
    public void 상품_목록_조회() throws Exception {
        RsData<List<ProductDto>> getProductListResponse = productService.getProductList();
        for(ProductDto productDto: getProductListResponse.getData()) {
            System.out.println(productDto);
        }
    }

    @Test
    public void 상품_데이터_상세_조회() throws Exception {
        Long id = 1L;
        RsData<ProductDetailResponse> productDetailResponse = productService.getProductDetail(id, null);
        System.out.println(productDetailResponse.getData());

        assertThat(id)
                .isEqualTo(productDetailResponse.getData().getId());
    }

    @Test
    public void 상품_데이터_수정() throws Exception {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setProductId(1L);
        productUpdateRequest.setStoreId(1L);
        productUpdateRequest.setCategoryId("CA01");
        productUpdateRequest.setProductTitle("[Update] 상품타이틀");
        productUpdateRequest.setProductDesc("[Update] 상품에 대한 설명입니다.");
        productUpdateRequest.setProductPrice(13000L);
        productUpdateRequest.setQualityTypeId("QU02");
        productUpdateRequest.setSalesTypeId("SA03");
        productUpdateRequest.setLocation("[Update] 서울시 강남구");
        productUpdateRequest.setCoolPrice(20000L);
        productUpdateRequest.setEndDays(2);

        RsData<ProductUpdateResponse> productUpdateResponse = productService.updateProduct(productUpdateRequest, new MultipartFile[0]);

        assertThat(productUpdateRequest.getProductTitle())
                .isEqualTo(productUpdateResponse.getData().getProductTitle());
    }

    @Test
    public void 상품_데이터_상태변경() throws Exception {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setProductId(1L);
        productUpdateRequest.setStatusTypeId("ST05"); // 삭제코드

        RsData<ProductUpdateResponse> productUpdateResponse = productService.updateProduct(productUpdateRequest, new MultipartFile[0]);

        assertThat(productUpdateRequest.getStatusTypeId())
                .isEqualTo(productUpdateResponse.getData().getStatusTypeId());
    }

    @Test
    public void 관심상품_토글() throws Exception {
        Long userId = 1L;
        Long productId = 3L;

        boolean result = productService.updateFavorite(userId, productId);

        assertThat(result).isTrue();
    }
}
