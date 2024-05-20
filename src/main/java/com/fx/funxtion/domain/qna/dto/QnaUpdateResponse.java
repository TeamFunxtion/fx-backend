package com.fx.funxtion.domain.qna.dto;

import com.fx.funxtion.domain.qna.entity.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QnaUpdateResponse {
    private Long id;
    private String qnaTitle;
    private String categoryId;
    private String qnaContent;
    private String qnaAnswer;
    private LocalDateTime createTime;

    public QnaUpdateResponse(Qna qna) {
        BeanUtils.copyProperties(qna, this);
    }
}
