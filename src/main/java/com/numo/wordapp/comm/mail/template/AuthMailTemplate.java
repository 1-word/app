package com.numo.wordapp.comm.mail.template;

import com.numo.wordapp.comm.mail.Mail;
import com.numo.wordapp.comm.mail.TemplateContent;
import com.numo.wordapp.comm.mail.TemplateVariable;

import java.util.List;

public class AuthMailTemplate implements MailTemplate {
    String to;
    String number;
    String subject;

    public AuthMailTemplate(String to, String number, String typeName) {
        this.to = to;
        this.number = number;
        if (typeName == null) {
            typeName = "";
        }
        subject = "VocaBox " + typeName + " 인증번호";
    }

    public Mail createMail() {
        TemplateVariable templateVariable = new TemplateVariable("number", number);
        TemplateContent templateContent = new TemplateContent("mail/auth", List.of(templateVariable));
        Mail mail = Mail.builder()
                .to(to)
                .subject(subject)
                .templateContent(templateContent)
                .build();
        return mail;
    }
}
