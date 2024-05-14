package com.fx.funxtion.domain.qna.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaCreateRequest {
    private MemberDto member;
    private String categoryId;
    private String qnaTitle;
    private String qnaContent;

}
