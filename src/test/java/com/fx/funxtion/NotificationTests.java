package com.fx.funxtion;

import com.fx.funxtion.domain.notification.service.NotificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NotificationTests {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void 알림_생성() {
        Long id = notificationService.createNotification(1L, 1L, "알림입니다!");

        Assertions.assertThat(id).isGreaterThan(0);
    }

    @Test void 알림_전체삭제() {
        notificationService.deleteAllNotifications(1L);
    }
}
