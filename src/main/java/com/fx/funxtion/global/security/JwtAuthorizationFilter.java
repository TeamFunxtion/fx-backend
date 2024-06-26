package com.fx.funxtion.global.security;

import com.fx.funxtion.domain.member.service.MemberService;
import com.fx.funxtion.global.RsData.RsData;
import com.fx.funxtion.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final Rq rq;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        System.out.println("JWTAuthorizationFilter doFilterInternal: ");
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().equals("/api/v1/members/login")
                || request.getRequestURI().equals("/api/v1/members/kakao/login")
                || request.getRequestURI().equals("/api/v1/members/logout")
                || request.getRequestURI().equals("/api/v1/members/join")
                || request.getRequestURI().equals("/api/v1/members/auth")
                || request.getRequestURI().equals("/api/v1/members")
                || request.getRequestURI().equals("/api/v1/notices")
                || request.getRequestURI().equals("/api/v1/faqs")
                || request.getRequestURI().startsWith("/api/v1/products")
                || request.getRequestURI().startsWith("/api/v1/notify")
                || request.getRequestURI().equals("/api/v1/reviews")
                || request.getRequestURI().startsWith("/api/v1/follow/sellerfollower")
                || request.getRequestURI().startsWith("/api/v1/follow/sellerfollowing")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = rq.getCookie("accessToken");
        // accessToken 검증 or refreshToken 발급
        if (!accessToken.isBlank()) {
            // 토큰 유효기간 검증
            if(!memberService.validateToken(accessToken)) { // accessToken이 유효하지않을때
                String refreshToken = rq.getCookie("refreshToken");

                RsData<String> rs =  memberService.refreshAccessToken(refreshToken);
                accessToken = rs.getData();
                rq.setCrossDomainCookie("accessToken", accessToken);
            }

            // securityUser 가져오기
            SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
            // 로그인 처리
            rq.setLogin(securityUser);
        }

        filterChain.doFilter(request, response);
    }
}
