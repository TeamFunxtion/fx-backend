package com.fx.funxtion.domain.notification.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.notification.controller.ApiV1NotificationController;
import com.fx.funxtion.domain.notification.dto.NotificationDto;
import com.fx.funxtion.domain.notification.dto.NotificationMessage;
import com.fx.funxtion.domain.notification.entity.Notification;
import com.fx.funxtion.domain.notification.repository.NotificationRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void notifyUser(String userId, NotificationMessage message) {
        try {
            ApiV1NotificationController.sendEventToUser(userId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long createNotification(Long receiverId, Long productId, String message) {
        Member member = memberRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        Notification notification = Notification.builder()
                .message(message)
                .member(member)
                .product(product)
                .build();
        notificationRepository.save(notification);

        return notification.getId();
    }

    public Page<NotificationDto> selectUserNotifications(Long userId, Pageable pageable) throws Exception {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return notificationRepository.findByMember(member, pageable)
                .map(NotificationDto::new);
    }

    public boolean deleteAllNotifications(Long userId) throws Exception {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        notificationRepository.deleteAllByMember(member);
        Long count = notificationRepository.countAllByMember(member);
        return count == 0;
    }
}
