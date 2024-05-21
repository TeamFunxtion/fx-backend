package com.fx.funxtion.domain.favorites.controller;

import com.fx.funxtion.domain.favorites.dto.UserFavoritesDto;
import com.fx.funxtion.domain.favorites.dto.UserFavoritesUpdateRequest;
import com.fx.funxtion.domain.favorites.service.UserFavoritesService;
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
public class ApiV1UserFavoritesController {

    private final UserFavoritesService userFavoritesService;

    // 관심상품 목록 조회
    @GetMapping("")
    public Page<UserFavoritesDto> getMyFavorites(@RequestParam(required = true, value = "userId") Long userId,
                                                 @RequestParam(required = false, defaultValue = "id", value = "sort") String sort,
                                                 @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                 @PageableDefault(size = 10, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), getPageableSort(sort));
        Page<UserFavoritesDto> pageList = userFavoritesService.getMyFavorites(userId, pageable);
        return pageList;
    }

    // 관심상품 상태 업데이트
    @PostMapping("")
    public RsData<Long> updateFavorites(@RequestBody UserFavoritesUpdateRequest userFavoritesUpdateRequest) {
        Long id = userFavoritesService.updateFavorites(userFavoritesUpdateRequest);
        return RsData.of("200", "관심상품 추가|삭제 성공!", id);
    }
}
