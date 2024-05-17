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
    private Long prCnt;
    private Long followerCnt;
    private boolean isFollowing;

    public FollowingListResponse(UserFollows userFollows, Long prCnt, Long followerCnt, boolean isFollowing) {
        this.id = userFollows.getId();
        this.toMember = new MemberDto(userFollows.getToMember());
        this.prCnt = prCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
    }
}
