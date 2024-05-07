package com.fx.funxtion;

import com.fx.funxtion.domain.product.dto.ProductCreateRequest;
import com.fx.funxtion.domain.product.dto.ProductCreateResponse;
import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.service.ProductService;
import com.fx.funxtion.global.RsData.RsData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import java.util.List;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductService productService;

    @Test
    public void 상품_데이터_등록_오픈형경매V2() {
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

        RsData<ProductCreateResponse> productCreateResponse = productService.createProduct(productCreateRequest);

        assertThat(productCreateRequest.getStoreId()).isEqualTo(productCreateResponse.getData().getStoreId());
    }

    @Test
    public void 상품_데이터_등록_일반판매() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest();
        productCreateRequest.setStoreId(1L);
        productCreateRequest.setCategoryId("CA01");
        productCreateRequest.setProductTitle("[일반판매]상품타이틀");
        productCreateRequest.setProductDesc("상품에 대한 설명입니다.");
        productCreateRequest.setProductPrice(13000L);
        productCreateRequest.setQualityTypeId("QU01");
        productCreateRequest.setSalesTypeId("SA01");
        productCreateRequest.setLocation("서울시 강남구");

        RsData<ProductCreateResponse> productCreateResponse = productService.createProduct(productCreateRequest);

        assertThat(productCreateRequest.getStoreId()).isEqualTo(productCreateResponse.getData().getStoreId());
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void getList() {
        List<ProductDto> productDtos = productService.getProductList();
        for(ProductDto productDto: productDtos) {
            System.out.println(productDto);
        }
    }

    @Test
    @DisplayName("상품 데이터 1개 조회")
    public void getOne() {
        Long id = 5L;
        RsData<ProductDto> getRs = productService.getProduct(id);
        System.out.println(getRs.getData());

        assertThat(id).isEqualTo(getRs.getData().getId());
    }

    @Test
    @DisplayName("상품 데이터 수정")
    public void update() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId("CA01");
        productDto.setProductTitle("상품타이틀22 Edit");
        productDto.setProductDesc("상품에 대한 설명입니다.222 Edit");
        productDto.setProductPrice(13000L);
        productDto.setQualityTypeId("QU02");
        productDto.setSalesTypeId("SA03");
        productDto.setLocation("서울시 강남구222 Edit");

        RsData<ProductDto> updateRs = productService.updateProduct(id, productDto);

        assertThat(productDto.getProductTitle()).isEqualTo(updateRs.getData().getProductTitle());
    }

    @Test
    @DisplayName("상품 데이터 상태 변경")
    public void changeStatus() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        productDto.setStatusTypeId("ST05"); // 삭제코드

        RsData<ProductDto> updateRs = productService.updateProduct(id, productDto);

        assertThat(productDto.getStatusTypeId()).isEqualTo(updateRs.getData().getStatusTypeId());
    }
}
