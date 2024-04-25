package com.fx.funxtion.global.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {
    @Getter
    private long id;

    public SecurityUser(long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
    }

    public Authentication genAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                this,
                this.getPassword(),
                this.getAuthorities()
        );

        return auth;
    }
}
