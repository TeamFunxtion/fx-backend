package com.fx.funxtion.domain.product.controller;

import com.fx.funxtion.domain.product.dto.FavoriteDto;
import com.fx.funxtion.domain.product.dto.FavoriteUpdateRequest;
import com.fx.funxtion.domain.product.service.FavoriteService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import static com.fx.funxtion.domain.product.service.ProductService.getPageableSort;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Slf4j
public class ApiV1FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 관심상품 목록 조회
     * @param userId
     * @param sort
     * @param pageNo
     * @param pageable
     * @return Page<FavoriteDto>
     */
    @GetMapping("")
    public Page<FavoriteDto> getMyFavorites(@RequestParam(required = true, value = "userId") Long userId,
                                            @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                            @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));
        Page<FavoriteDto> pageList = null;
        try {
            pageList = favoriteService.getMyFavorites(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageList;
    }

    /**
     * 관심상품 상태 업데이트
     * @param favoriteUpdateRequest
     * @return RsData<Long>
     */
    @PostMapping("")
    public RsData<Long> updateFavorites(@RequestBody FavoriteUpdateRequest favoriteUpdateRequest) {
        try {
            Long id = favoriteService.updateFavorites(favoriteUpdateRequest);
            return RsData.of("200", "관심상품 추가|삭제 성공!", id);
        } catch (Exception e) {
            return RsData.of("500", e.getMessage());
        }
    }
}
