package com.fx.funxtion;

import com.fx.funxtion.domain.product.dto.ProductDto;
import com.fx.funxtion.domain.product.service.ProductService;
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
    @DisplayName("상품 데이터 등록")
    public void register() {
        ProductDto productDto = new ProductDto();
        productDto.setStoreId(1);
        productDto.setCategoryId("CA01");
        productDto.setProductTitle("상품타이틀");
        productDto.setProductDesc("상품에 대한 설명입니다.");
        productDto.setProductPrice(13000);
        productDto.setQualityTypeId("QU01");
        productDto.setSalesTypeId("SA01");
        productDto.setLocation("서울시 강남구");

        ProductDto p = productService.registerProduct(productDto);

        assertThat(productDto.getStoreId()).isEqualTo(p.getStoreId());
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
        long productId = 5L;

        ProductDto productDto = productService.getProduct(productId);
        System.out.println(productDto);

        assertThat(productId).isEqualTo(productDto.getId());
    }

    @Test
    @DisplayName("상품 데이터 수정")
    public void update() {
        ProductDto productDto = new ProductDto();
        productDto.setId(5L);
        productDto.setStoreId(1);
        productDto.setCategoryId("CA01");
        productDto.setProductTitle("상품타이틀22 Edit");
        productDto.setProductDesc("상품에 대한 설명입니다.222 Edit");
        productDto.setProductPrice(13000);
        productDto.setQualityTypeId("QU02");
        productDto.setSalesTypeId("SA03");
        productDto.setLocation("서울시 강남구222 Edit");

        ProductDto p = productService.editProduct(productDto);

        assertThat(productDto.getProductTitle()).isEqualTo(p.getProductTitle());
    }

    @Test
    @DisplayName("상품 데이터 상태 변경")
    public void changeStatus() {
        ProductDto productDto = new ProductDto();
        productDto.setId(5L);
        productDto.setStatusTypeId("ST05"); // 삭제코드

        ProductDto p = productService.changeStatus(productDto);

        assertThat(productDto.getStatusTypeId()).isEqualTo(p.getStatusTypeId());
    }
}
