package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.dto.*;
import com.fx.funxtion.domain.product.entity.*;
import com.fx.funxtion.domain.product.repository.BidRepository;
import com.fx.funxtion.domain.product.repository.FavoriteRepository;
import com.fx.funxtion.domain.product.repository.ProductImageRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.util.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final BidRepository bidRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageService imageService;

    public RsData<ProductCreateResponse> createProduct(ProductCreateRequest productCreateRequest, MultipartFile[] multipartFiles) {
        Member member = memberRepository.findById(productCreateRequest.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        Product product = Product.builder()
                .member(member)
                .categoryId(productCreateRequest.getCategoryId())
                .productTitle(productCreateRequest.getProductTitle())
                .productDesc(productCreateRequest.getProductDesc())
                .productPrice(productCreateRequest.getProductPrice())
                .qualityTypeId(productCreateRequest.getQualityTypeId())
                .salesTypeId(productCreateRequest.getSalesTypeId())
                .statusTypeId(ProductStatusType.ST01.name())
                .location(productCreateRequest.getLocation())
                .currentPrice(productCreateRequest.getProductPrice())
                .coolPrice(productCreateRequest.getCoolPrice())
                .endTime(LocalDateTime.now().plusDays(productCreateRequest.getEndDays()))
                .build();
        productRepository.save(product);

        if(multipartFiles != null && multipartFiles.length > 0) { // 이미지가 있을때
            for(int i=0; i<multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                try {
                    String imageUrl = imageService.uploadImage(file); // 이미지를 S3에 업로드

                    ProductImage img = ProductImage.builder()
                            .product(product)
                            .originalName(file.getOriginalFilename())
                            .size(file.getSize())
                            .imageUrl(imageUrl)
                            .mainYn("N")
                            .build();
                    productImageRepository.save(img);

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        Optional<Product> optionalProduct = productRepository.findById(product.getId());

        return optionalProduct.map(p -> RsData.of("200", "상품 등록 성공!", new ProductCreateResponse(p)))
                .orElseGet(() -> RsData.of("500", "상품 등록 실패!"));
    }


    public RsData<ProductUpdateResponse> updateProduct(ProductUpdateRequest productUpdateRequest, MultipartFile[] multipartFiles) {
        Optional<Product> optionalProduct = productRepository.findById(productUpdateRequest.getProductId());
        System.out.println(optionalProduct.isEmpty());

        if (optionalProduct.isEmpty()) {
            return RsData.of("500", "상품이 존재하지 않습니다!");
        }

        Product p = optionalProduct.get();

        if (p.getStatusTypeId().equals(ProductStatusType.ST01.name()) && !p.getBids().isEmpty()) { // 입찰내역이 있는 경매진행중인 상품이라면
            return RsData.of("500", "해당 상품은 입찰내역이 있는 경매가 진행중인 상품입니다.");
        }

        if (productUpdateRequest.getProductTitle() != null && !productUpdateRequest.getProductTitle().isEmpty()) { // 상품명
            p.setProductTitle(productUpdateRequest.getProductTitle());
        }
        if (productUpdateRequest.getCategoryId() != null && !productUpdateRequest.getCategoryId().isEmpty()) { // 카테고리
            p.setCategoryId(productUpdateRequest.getCategoryId());
        }
        if (productUpdateRequest.getProductPrice() != null) { // 가격
            p.setProductPrice(productUpdateRequest.getProductPrice());
            p.setCurrentPrice(productUpdateRequest.getProductPrice());
        }
        if (productUpdateRequest.getProductDesc() != null && !productUpdateRequest.getProductDesc().isEmpty()) { // 설명
            p.setProductDesc(productUpdateRequest.getProductDesc());
        }
        if (productUpdateRequest.getQualityTypeId() != null && !productUpdateRequest.getQualityTypeId().isEmpty()) { // 품질상태
            p.setQualityTypeId(productUpdateRequest.getQualityTypeId());
        }
        if (productUpdateRequest.getLocation() != null && !productUpdateRequest.getLocation().isEmpty()) { // 거래지역
            p.setLocation(productUpdateRequest.getLocation());
        }
        if (productUpdateRequest.getSalesTypeId() != null && !productUpdateRequest.getSalesTypeId().isEmpty()) { // 판매방식
            p.setSalesTypeId(productUpdateRequest.getSalesTypeId());
        }
        if (productUpdateRequest.getStatusTypeId() != null && !productUpdateRequest.getStatusTypeId().isEmpty()) { // 상품상태
            p.setStatusTypeId(productUpdateRequest.getStatusTypeId());
        }
        if (productUpdateRequest.getCoolPrice() != null) { // 즉구가
            p.setCoolPrice(productUpdateRequest.getCoolPrice());
        }
        if (productUpdateRequest.getEndDays() > 0) { // 경매 종료일
            p.setEndTime(LocalDateTime.now().plusDays(productUpdateRequest.getEndDays()));
        }

        productRepository.save(p);

        if(multipartFiles != null && multipartFiles.length > 0) { // 이미지가 있을때
            for(int i=0; i<multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                try {
                    String imageUrl = imageService.uploadImage(file); // 이미지를 S3에 업로드

                    ProductImage img = ProductImage.builder()
                            .product(p)
                            .originalName(file.getOriginalFilename())
                            .size(file.getSize())
                            .imageUrl(imageUrl)
                            .mainYn("N")
                            .build();
                    productImageRepository.save(img);

                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        if(!productUpdateRequest.getRemoveImgIds().isEmpty()) { // 삭제할 이미지가 있으면
            productImageRepository.deleteAllByIdInBatch(productUpdateRequest.getRemoveImgIds());
        }

        return RsData.of("200", "상품 수정 성공!", new ProductUpdateResponse(p));
    }

    public RsData<ProductUpdateResponse> changeProductStatus(ProductUpdateRequest productUpdateRequest) {
        Optional<Product> optionalProduct = productRepository.findById(productUpdateRequest.getProductId());
        System.out.println(optionalProduct.isEmpty());

        if (optionalProduct.isEmpty()) {
            return RsData.of("500", "상품이 존재하지 않습니다!");
        }

        Product p = optionalProduct.get();

        if (p.getStatusTypeId().equals(ProductStatusType.ST01.name()) && !p.getBids().isEmpty()) { // 입찰내역이 있는 경매진행중인 상품이라면
            return RsData.of("500", "해당 상품은 입찰내역이 있는 경매가 진행중인 상품입니다.");
        }

        if (productUpdateRequest.getProductTitle() != null && !productUpdateRequest.getProductTitle().isEmpty()) { // 상품명
            p.setProductTitle(productUpdateRequest.getProductTitle());
        }
        if (productUpdateRequest.getCategoryId() != null && !productUpdateRequest.getCategoryId().isEmpty()) { // 카테고리
            p.setCategoryId(productUpdateRequest.getCategoryId());
        }
        if (productUpdateRequest.getProductPrice() != null) { // 가격
            p.setProductPrice(productUpdateRequest.getProductPrice());
            p.setCurrentPrice(productUpdateRequest.getProductPrice());
        }
        if (productUpdateRequest.getProductDesc() != null && !productUpdateRequest.getProductDesc().isEmpty()) { // 설명
            p.setProductDesc(productUpdateRequest.getProductDesc());
        }
        if (productUpdateRequest.getQualityTypeId() != null && !productUpdateRequest.getQualityTypeId().isEmpty()) { // 품질상태
            p.setQualityTypeId(productUpdateRequest.getQualityTypeId());
        }
        if (productUpdateRequest.getLocation() != null && !productUpdateRequest.getLocation().isEmpty()) { // 거래지역
            p.setLocation(productUpdateRequest.getLocation());
        }
        if (productUpdateRequest.getSalesTypeId() != null && !productUpdateRequest.getSalesTypeId().isEmpty()) { // 판매방식
            p.setSalesTypeId(productUpdateRequest.getSalesTypeId());
        }
        if (productUpdateRequest.getStatusTypeId() != null && !productUpdateRequest.getStatusTypeId().isEmpty()) { // 상품상태
            p.setStatusTypeId(productUpdateRequest.getStatusTypeId());
        }
        if (productUpdateRequest.getCoolPrice() != null) { // 즉구가
            p.setCoolPrice(productUpdateRequest.getCoolPrice());
        }
        if (productUpdateRequest.getEndDays() > 0) { // 경매 종료일
            p.setEndTime(LocalDateTime.now().plusDays(productUpdateRequest.getEndDays()));
        }

        productRepository.save(p);

        return RsData.of("200", "상품 상태 변경 성공!", new ProductUpdateResponse(p));
    }

    @Transactional
    public Page<ProductDto> searchByKeyword(String keyword, Pageable pageable) {
        Page<ProductDto> list = productRepository.findByProductTitleContainingAndStatusTypeId(keyword, "ST01", pageable)
                .map(ProductDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

    public Page<ProductDto> searchByCategory(String category, Pageable pageable) {
        Page<ProductDto> list = productRepository.findByCategoryIdAndStatusTypeId(category, "ST01", pageable)
                .map(ProductDto::new);
        return list; // RsData.of("200", "목록 조회 성공!", list);
    }

    public Page<ProductDto> getMyProducts(Long userId, String statusTypeId, Pageable pageable) {
        Optional<Member> member = memberRepository.findById(userId);

        Page<ProductDto> results = productRepository.findByMemberAndStatusTypeId(member.get(), statusTypeId, pageable)
                .map(ProductDto::new);
        return results;
    }

    public RsData<List<ProductDto>> getProductList() {
        List<ProductDto> list = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return RsData.of("200", "목록 조회 성공!", list);
    }

    public RsData<ProductDetailResponse> getProductDetail(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isEmpty()) {
            return RsData.of("500", "상품 조회 실패!");
        }

        ProductDetailResponse productDetailResponse = new ProductDetailResponse(optionalProduct.get());
        if (userId != null) {
            Favorite favor = favoriteRepository.findByUserIdAndProductId(userId, productId);
            productDetailResponse.setFavorite(favor != null);
        }

        return RsData.of("200", "상품 조회 성공!", productDetailResponse);
    }

    @Transactional
    public int increaseViews(Long id) {
        return productRepository.increaseViews(id);
    }


    public boolean updateFavorite(Long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(userId, productId);

        if (favorite != null) {
            favoriteRepository.delete(favorite);
            return false;
        } else {
            Product product = productRepository.findById(productId).get();

            favorite = Favorite.builder()
                    .userId(userId)
                    .product(product)
                    .build();

            favoriteRepository.save(favorite);
            return true;
        }
    }

    public static Sort getPageableSort(String sort) {
        if (sort.equals("price_asc") || sort.equals("price_desc")) {
            return Sort.by(sort.equals("price_asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "currentPrice");
        } else {
            return Sort.by(Sort.Direction.DESC, sort);
        }
    }

    public Page<ProductDto> getAuctionProducts(Long userId, String statusTypeId, Pageable pageable) {
        Optional<Member> member = memberRepository.findById(userId);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        Page<ProductDto> results = productRepository.findByMemberAndSalesTypeIdNotAndStatusTypeId(
                member.get(),
                "SA03", // salesTypeId가 3이 아닌 상품들만 조회합니다.
                statusTypeId, // 상태가 활성인 상품만 조회합니다.
                pageable).map(ProductDto::new);

        return results;
    }
    public Page<ProductDto> getBidProducts(Long userId, Pageable pageable) {
        Optional<Member> member = memberRepository.findById(userId);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        Page<ProductDto> results = bidRepository.findWithProductUsingJoinByMember(member.get().getId(), pageable);


        System.out.println("------------------------------------");


        return results;

    }
}