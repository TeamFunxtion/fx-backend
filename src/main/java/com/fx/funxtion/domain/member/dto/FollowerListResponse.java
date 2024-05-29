package com.fx.funxtion.domain.member.dto;

import com.fx.funxtion.domain.member.entity.UserFollows;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class FollowerListResponse {
    private Long id;
    private MemberDto fromMember;
    private Long productCnt;
    private Long followerCnt;
    private boolean isFollowing;
    private int followCnt;
    private long rating;

    public FollowerListResponse(UserFollows userFollows, Long productCnt, Long followerCnt, boolean isFollowing, int followCnt, long rating) {
        this.id = userFollows.getId();
        this.fromMember = new MemberDto(userFollows.getFromMember());
        this.productCnt = productCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
        this.followCnt = followCnt;
        this.rating = rating;
    }
}
