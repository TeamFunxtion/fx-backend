package com.fx.funxtion.domain.follow.dto;

import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.follow.entity.UserFollows;
import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.dto.ProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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

    public FollowerListResponse(UserFollows userFollows, Long productCnt, Long followerCnt, boolean isFollowing, int followCnt) {
        this.id = userFollows.getId();
        this.fromMember = new MemberDto(userFollows.getFromMember());
        this.productCnt = productCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
        this.followCnt = followCnt;
    }
}
