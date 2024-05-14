package com.fx.funxtion.domain.qna.dto;

import com.fx.funxtion.domain.member.dto.MemberDto;
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
public class QnaDto {
    private Long id;
    private MemberDto userId;
    private String qnaTitle;
    private String qnaContent;
    private String qnaAnswer;
    private LocalDateTime createDate;


    public QnaDto(Qna qna) {
        BeanUtils.copyProperties(qna, this);
        this.id = qna.getId();
        this.userId = new MemberDto(qna.getMember());
        this.qnaTitle = qna.getQnaTitle();
        this.qnaContent = qna.getQnaContent();
        this.createDate = qna.getCreateDate();


    }
}