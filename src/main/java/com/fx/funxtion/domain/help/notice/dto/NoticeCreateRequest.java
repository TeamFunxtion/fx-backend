package com.fx.funxtion.domain.help.notice.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeCreateRequest {

    private String noticeTitle;
    private String noticeContent;

}
