package com.fx.funxtion.global.initData;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
public class NotProd {

//    @Bean
//    CommandLineRunner initData(MemberService memberService, PasswordEncoder passwordEncoder) {
//        String password = passwordEncoder.encode("1234");
//        return args -> {
//            Member m1 = memberService.join("admin@test.com", password);
//        };
//    }
}
