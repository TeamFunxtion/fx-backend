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
    private Long prCnt;
    private Long followerCnt;
    private boolean isFollowing;

    public FollowerListResponse(UserFollows userFollows, Long prCnt, Long followerCnt, boolean isFollowing) {
        this.id = userFollows.getId();
        this.fromMember = new MemberDto(userFollows.getFromMember());
        this.prCnt = prCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
    }
}
