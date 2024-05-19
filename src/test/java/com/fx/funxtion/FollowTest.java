package com.fx.funxtion;


import com.fx.funxtion.domain.chat.dto.ChatRoomWithMessagesDto;
import com.fx.funxtion.domain.follow.dto.FollowerListResponse;
import com.fx.funxtion.domain.follow.dto.FollowingListResponse;
import com.fx.funxtion.domain.follow.repository.FollowRepository;
import com.fx.funxtion.domain.follow.service.FollowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FollowTest {
    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Test
    @DisplayName("팔로워 목록 조회")
    public void getFollowerList() {
        Long toId = 1L;
        List<FollowerListResponse> followerList = followService.getFollowerList(toId);

        for(FollowerListResponse followerListResponse: followerList) {
            System.out.println(followerListResponse.getFromMember().getNickname());
        }
    }

    @Test
    @DisplayName("팔로잉 목록 조회")
    public void getFollowingList() {
        Long fromId = 1L;
        List<FollowingListResponse> followingList = followService.getFollowingList(fromId);

        for(FollowingListResponse followingListResponse: followingList) {
            System.out.println(followingListResponse.getToMember().getNickname());
        }
    }

}
