package com.fx.funxtion.domain.notification.dto;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fx.funxtion.domain.member.dto.MemberDto;
import com.fx.funxtion.domain.notification.entity.Notification;
import com.fx.funxtion.domain.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;

    private String message;

    private ProductDto productDto;

    private MemberDto memberDto;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public NotificationDto(Notification notification) {
        BeanUtils.copyProperties(notification, this);
        this.memberDto = new MemberDto(notification.getMember());
        this.productDto = new ProductDto(notification.getProduct());
    }
}
