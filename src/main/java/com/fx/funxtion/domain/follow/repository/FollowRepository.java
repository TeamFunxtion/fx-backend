package com.fx.funxtion.domain.follow.repository;

import com.fx.funxtion.domain.follow.entity.UserFollows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<UserFollows, Long> {

    List<UserFollows> findAllByToMemberId(Long toMemberId);

    List<UserFollows> findAllByFromMemberId(Long fromMemberId);

    Page<UserFollows> findAllByToMemberId(Long toMemberId, Pageable pageable);

    Page<UserFollows> findAllByFromMemberId(Long fromMemberId, Pageable pageable);

    Long countByToMemberId(Long toMemberId);

    boolean existsByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);

    UserFollows findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);


}
