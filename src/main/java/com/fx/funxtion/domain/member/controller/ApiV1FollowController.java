package com.fx.funxtion.domain.member.controller;


import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.service.FollowService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
@Slf4j
public class ApiV1FollowController {

    private final FollowService followService;

    // 팔로워 목록 조회
    @GetMapping("/follower")
    public RsData<Page<FollowerListResponse>> getFollowerList(@RequestParam("userId") String userId,
                                                              @RequestParam(value= "page", defaultValue = "0")int page,
                                                              @RequestParam(value="size", defaultValue = "10")int size) {
        Long toId = Long.parseLong(userId);
        Page<FollowerListResponse> list = followService.getFollowerList(toId, page, size);

        return RsData.of("200", "팔로우 목록 조회 성공!", list);
    }

    // 팔로잉 목록 조회
    @GetMapping("following")
    public RsData<Page<FollowingListResponse>> getFollowingList(@RequestParam("userId") String userId,
                                                                @RequestParam(value= "page", defaultValue = "0")int page,
                                                                @RequestParam(value="size", defaultValue = "10")int size) {
        Long fromId = Long.parseLong(userId);
        Page<FollowingListResponse> list = followService.getFollowingList(fromId, page, size);

        return RsData.of("200", "팔로잉 목록 조회 성공!", list);
    }

    // 판매자 팔로워 목록 조회
    @GetMapping("/sellerfollower")
    public RsData<Page<SellerFollowerListResponse>> getSellerFollowerList(@RequestParam(name="userId", defaultValue="") String userId,
                                                                          @RequestParam("storeId") String storeId,
                                                                          @RequestParam(value= "page", defaultValue = "0")int page,
                                                                          @RequestParam(value="size", defaultValue = "10")int size) {
        Long toId = Long.parseLong(storeId);
        Long user = 0L;
        if(userId.equals("")) {
            user = 0L;
        } else {
            user = Long.parseLong(userId);
        }
//        Long user = Long.parseLong(userId);
        Page<SellerFollowerListResponse> list = followService.getSellerFollowerList(toId, user, page, size);

        return RsData.of("200", "팔로우 목록 조회 성공!", list);
    }

    // 판매자 팔로잉 목록 조회
    @GetMapping("sellerfollowing")
    public RsData<Page<SellerFollowingListResponse>> getSellerFollowingList(@RequestParam(name = "userId", defaultValue = "") String userId ,
                                                                            @RequestParam("storeId") String storeId,
                                                                            @RequestParam(value= "page", defaultValue = "0")int page,
                                                                            @RequestParam(value="size", defaultValue = "10")int size) {
        Long fromId = Long.parseLong(storeId);
        Long user = 0L;
        if(userId.equals("")) {
            user = 0L;
        } else {
            user = Long.parseLong(userId);
        }
//        Long user = Long.parseLong(userId);
        Page<SellerFollowingListResponse> list = followService.getSellerFollowingList(fromId, user, page, size);

        return RsData.of("200", "팔로잉 목록 조회 성공!", list);
    }


    // 팔로우 추가 or 해제
    @PostMapping("follower")
    public RsData<Long> updateFollow(@RequestBody FollowUpdateRequest followUpdateRequest) {

        Long id = followService.updateFollow(followUpdateRequest);

        return RsData.of("200", "팔로우 추가|해제 성공", id);
    }
}
