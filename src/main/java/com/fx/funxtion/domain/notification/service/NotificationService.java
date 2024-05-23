package com.fx.funxtion.domain.notification.service;

import com.fx.funxtion.domain.notification.controller.ApiV1NotificationController;
import com.fx.funxtion.domain.notification.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApiV1NotificationController notificationController;

    public void notifyUser(String userId, NotificationMessage message) {
        try {
            notificationController.sendEventToUser(userId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
