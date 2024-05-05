package com.fx.funxtion;

import com.fx.funxtion.global.util.mail.MailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SendMailTest {

    @Autowired
    private MailUtils mailUtils;

    @Test
    public void sendPasswordMail() {
        String to = "sksmsdirjsdnl@gmail.com";

        MailUtils.SendMailResponseBody sendMailRs = mailUtils.sendMail(to, "password");

        System.out.println(sendMailRs.getAuthCode());
        System.out.println(sendMailRs.isResult());
    }

    @Test
    public void sendJoinMail() {
        String to = "sksmsdirjsdnl@gmail.com";

        MailUtils.SendMailResponseBody sendMailRs = mailUtils.sendMail(to, "email");

        System.out.println(sendMailRs.getAuthCode());
        System.out.println(sendMailRs.isResult());

    }
}
