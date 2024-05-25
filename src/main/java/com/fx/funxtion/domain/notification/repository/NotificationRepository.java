package com.fx.funxtion.domain.notification.repository;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByMember(Member member, Pageable pageable);

    @Transactional
    void deleteAllByMember(Member member);

    Long countAllByMember(Member member);
}
