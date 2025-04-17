package com.numo.api.global.comm.mail.template;

import com.numo.api.global.comm.mail.Mail;
import com.numo.api.global.comm.mail.TemplateContent;
import com.numo.api.global.comm.mail.TemplateVariable;

import java.util.List;

public class ShareMailTemplate implements MailTemplate {
    String to;
    String wordBookName;
    String nickname;
    String link;
    String subject;

    public ShareMailTemplate(String to, String wordBookName, String nickname, String link) {
        this.to = to;
        this.wordBookName = wordBookName;
        this.nickname = nickname;
        this.link = link;
        subject = "VocaBox - " + wordBookName + " 단어장에 초대되었습니다.";
    }

    public Mail createMail() {
        TemplateVariable wordBookNameVar = new TemplateVariable("wordBookName", wordBookName);
        TemplateVariable nicknameVar = new TemplateVariable("nickname", nickname);
        TemplateVariable linkVar = new TemplateVariable("link", link);
        TemplateContent templateContent = new TemplateContent("mail/share", List.of(wordBookNameVar, nicknameVar, linkVar));
        Mail mail = Mail.builder()
                .to(to)
                .subject(subject)
                .templateContent(templateContent)
                .build();
        return mail;
    }
}
