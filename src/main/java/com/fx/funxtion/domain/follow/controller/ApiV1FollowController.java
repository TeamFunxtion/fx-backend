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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follow")
@RequiredArgsConstructor
@Slf4j
public class ApiV1FollowController {
    @Autowired
    private final FollowService followService;

    // 팔로워 목록 조회
    @GetMapping("/follower")
    public RsData<List> getFollowerList(@RequestParam("userId") String userId) {
        Long toId = Long.parseLong(userId);
        List<FollowerListResponse> list = followService.getFollowerList(toId);
        return RsData.of("200", "채팅방 조회 성공!", list);
    }

    // 팔로잉 목록 조회
    @GetMapping("following")
    public RsData<List> getFollowingList(@RequestParam("userId") String userId) {
        Long fromId = Long.parseLong(userId);
        List<FollowingListResponse> list = followService.getFollowingList(fromId);
        return RsData.of("200", "팔로잉 목록 조회 성공!", list);
    }


    // 팔로우 추가 or 해제
    @PostMapping("follower")
    public RsData<Long> updateFollow(@RequestBody FollowUpdateRequest followUpdateRequest) {

        Long id = followService.updateFollow(followUpdateRequest);
        if(id == null) {
            id = 999999L;
        }
        return RsData.of("200", "팔로우 추가|해제 성공", id);
    }
}
