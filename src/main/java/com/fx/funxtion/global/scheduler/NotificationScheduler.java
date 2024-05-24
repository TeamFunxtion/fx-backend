package com.fx.funxtion.global.scheduler;


import com.fx.funxtion.domain.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;

//    @Scheduled(cron = "0 0/1 * * * ?") // 매분마다 실행
    public void scheduleNotification() {
        String userId = "2"; // 예시로 특정 사용자 ID를 설정
        String message = "맙소사 낙찰됐어요";
//        notificationService.notifyUser(userId, message);
    }
}