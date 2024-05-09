package com.fx.funxtion.domain.notice.dto;

import com.fx.funxtion.domain.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class NoticeDto {
    private long id;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public NoticeDto(Notice notice){
        this.id = notice.getId();
        this.title = notice.getNoticeTitle();
        this.content = notice.getNoticeContent();
        this.createDate = notice.getCreateDate();
        this.updateDate = notice.getCreateDate();
    }
}
