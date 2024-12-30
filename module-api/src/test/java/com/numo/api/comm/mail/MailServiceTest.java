package com.numo.api.comm.mail;

import com.numo.api.global.comm.mail.Mail;
import com.numo.api.global.comm.mail.MailService;
import com.numo.api.global.comm.mail.TemplateContent;
import com.numo.api.global.comm.mail.TemplateVariable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    void send() {
        TemplateVariable templateVariable = new TemplateVariable("number", 1234567);
        TemplateContent templateContent = new TemplateContent("mail/auth", List.of(templateVariable));
        Mail mail = Mail.builder()
                .to("")
                .subject("hello")
                .templateContent(templateContent)
                .build();

        mailService.send(mail);
    }
}