package com.fx.funxtion.domain.help.notice.dto;


import com.fx.funxtion.domain.help.notice.entity.Notice;
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
public class NoticeCreateResponse {

    private Long id;
    private String noticeTitle;
    private String noticeContent;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public NoticeCreateResponse(Notice notice) {
        BeanUtils.copyProperties(notice, this);}

}
