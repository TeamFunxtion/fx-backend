package com.fx.funxtion.domain.help.qna.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaCreateRequest {
    private Long userId;
    private String categoryId;
    private String qnaTitle;
    private String qnaContent;

}


