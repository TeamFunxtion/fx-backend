package com.fx.funxtion.domain.follow.dto;

import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.follow.entity.UserFollows;
import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.product.dto.ProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@NoArgsConstructor
@ToString
@Setter
public class UserFollowsDto {
    private Long id;
    private MemberDto fromMember;
    private MemberDto toMember;

    public UserFollowsDto(UserFollows userFollows) {
        this.id = userFollows.getId();
        this.fromMember = new MemberDto(userFollows.getFromMember());
        this.toMember = new MemberDto(userFollows.getToMember());
    }
}
