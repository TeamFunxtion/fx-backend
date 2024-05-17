package com.fx.funxtion.domain.follow.dto;

import com.fx.funxtion.domain.follow.entity.UserFollows;
import com.fx.funxtion.domain.member.dto.MemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class FollowingListResponse {
    private Long id;
    private MemberDto toMember;
    private Long productCnt;
    private Long followerCnt;
    private boolean isFollowing;
    private int followCnt;

    public FollowingListResponse(UserFollows userFollows, Long productCnt, Long followerCnt, boolean isFollowing, int followCnt) {
        this.id = userFollows.getId();
        this.toMember = new MemberDto(userFollows.getToMember());
        this.productCnt = productCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
        this.followCnt = followCnt;
    }
}
