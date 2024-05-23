package com.fx.funxtion.domain.follow.controller;


import com.fx.funxtion.domain.chat.dto.ChatRoomCreateRequest;
import com.fx.funxtion.domain.chat.dto.ChatRoomWithMessagesDto;
import com.fx.funxtion.domain.follow.dto.FollowUpdateRequest;
import com.fx.funxtion.domain.follow.dto.FollowerListResponse;
import com.fx.funxtion.domain.follow.dto.FollowingListResponse;
import com.fx.funxtion.domain.follow.service.FollowService;
import com.fx.funxtion.global.RsData.RsData;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return RsData.of("200", "채팅방 조회 성공!", list);
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



    // 팔로우 추가 or 해제
    @PostMapping("follower")
    public RsData<Long> updateFollow(@RequestBody FollowUpdateRequest followUpdateRequest) {

        Long id = followService.updateFollow(followUpdateRequest);

        return RsData.of("200", "팔로우 추가|해제 성공", id);
    }
}
