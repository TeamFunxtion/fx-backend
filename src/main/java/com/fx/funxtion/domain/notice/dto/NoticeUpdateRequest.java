package com.fx.funxtion.domain.notice.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeUpdateRequest {

    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
}
