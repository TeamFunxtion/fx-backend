package com.fx.funxtion.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationMessage<T> {
    private String type;
    private String message;
    private T data;
}
