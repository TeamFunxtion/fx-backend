package com.fx.funxtion.global.util.mail;

import com.fx.funxtion.global.util.mail.dto.EmailMessage;
import com.fx.funxtion.global.util.mail.dto.MailProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtils {
    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;


    @Getter
    @AllArgsConstructor
    public static class SendMailResponseBody {
        private String authCode;
        private boolean result;
    }


    public SendMailResponseBody sendMail(String to, String type) {
        String authNum = createAuthNum();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .subject(String.format("[Funxtion] %s", type.equals("password") ? "임시 비밀번호 발급" : "이메일 인증을 위한 인증코드 발송"))
                .build();

        if(type.equals("password")) {
            // todo. 생성한 인증번호로 회원의 비밀번호 변경
        }

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(mailProperties.getFromMail());
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(emailMessage.getTo(), authNum, type), true);
            javaMailSender.send(mimeMessage);

            log.info("[SUCCESS] EmailService.sendMail to -> " + to);

            return new SendMailResponseBody(authNum, true);

        } catch (MessagingException e) {
            log.info("[FAIL] EmailService.sendMail to -> " + to);

            return new SendMailResponseBody(authNum, false);
        }
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    private String createAuthNum() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String to, String code, String type) {
        Context context = new Context();
        context.setVariable("email", to);
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
