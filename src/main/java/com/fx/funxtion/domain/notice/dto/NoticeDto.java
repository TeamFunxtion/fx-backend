package com.fx.funxtion.domain.notice.dto;

import com.fx.funxtion.domain.notice.entity.Notice;
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
public class NoticeDto {
    private long id;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;


    public NoticeDto(Notice notice){
        BeanUtils.copyProperties(notice,this);

    }
}
