package com.fx.funxtion.global.rq;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.global.security.SecurityUser;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequestScope
@RequiredArgsConstructor
public class Rq {
    private final HttpServletResponse resp;
    private final HttpServletRequest req;
    private final EntityManager entityManager;
    private Member member;

    public String getCookie(String name) {
        Cookie[] cookies = req.getCookies();

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }

    public void setCrossDomainCookie(String tokenName, String token) {
        ResponseCookie cookie = ResponseCookie.from(tokenName, token)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    private void removeCrossDomainCookie(String tokenName) {
        ResponseCookie cookie = ResponseCookie.from(tokenName, null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }


    public Member getMember() {
        if(isLogout()) return null;

        if(member == null) {
            member = entityManager.getReference(Member.class, getUser().getId());
        }

        return member;
    }

    public void setLogin(SecurityUser securityUser) {
        SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
    }

    private SecurityUser getUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(context -> context.getAuthentication())
                .filter(authentication -> authentication.getPrincipal() instanceof SecurityUser)
                .map(authentication -> (SecurityUser) authentication.getPrincipal())
                .orElse(null);
    }

    private boolean isLogin() {
        return getUser() != null;
    }

    private boolean isLogout() {
        return !isLogin();
    }

    public void logout() {
        removeCrossDomainCookie("accessToken");
        removeCrossDomainCookie("refreshToken");
    }
}
