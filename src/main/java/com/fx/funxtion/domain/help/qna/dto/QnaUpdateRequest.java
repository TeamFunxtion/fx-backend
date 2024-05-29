package com.fx.funxtion.domain.help.qna.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaUpdateRequest {
    private Long id;
    private String qnaAnswer;
}

