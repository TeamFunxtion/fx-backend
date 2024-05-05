package com.fx.funxtion.domain.member.repository;

import com.fx.funxtion.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndDeleteYn(String email, String deleteYn);
    Optional<Member> findByRefreshToken(String refreshToken);
    void deleteByEmail(String email);
}
