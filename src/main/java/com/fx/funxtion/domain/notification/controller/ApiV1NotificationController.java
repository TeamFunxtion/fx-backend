package com.fx.funxtion.domain.notification.controller;

import com.fx.funxtion.domain.notification.dto.NotificationDto;
import com.fx.funxtion.domain.notification.dto.NotificationMessage;
import com.fx.funxtion.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
public class ApiV1NotificationController {

    private final NotificationService notificationService;
    private static final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/events/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents(@PathVariable(name="userId") String userId) {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        return emitter;
    }

    public static void sendEventToUser(String userId, NotificationMessage message) throws IOException {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            emitter.send(SseEmitter.event().name("message").data(message));
        }
    }

    /**
     * 알림 목록 조회
     * @param userId
     * @param pageable
     * @return Page<NotificationDto>
     */
    @GetMapping("")
    public Page<NotificationDto> selectUserNotifications(
            @RequestParam(name="id") Long userId,
            @PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        int pageNo = pageable.getPageNumber();
        pageNo = pageNo == 0 ? pageNo : pageNo -1;
        pageable = PageRequest.of(pageNo, pageable.getPageSize(), pageable.getSort());
        Page<NotificationDto> result = null;
        try {
            result = notificationService.selectUserNotifications(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 알림 전체 삭제
     * @param userId
     * @return boolean
     */
    @DeleteMapping("")
    public boolean deleteNotifications(@RequestParam(name="id") Long userId) {
        try {
            return notificationService.deleteAllNotifications(userId);
        } catch (Exception e) {
            return false;
        }
    }
}
