package com.numo.wordapp.comm.mail;

import com.numo.wordapp.conf.MailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MailConfig mailConfig;

    /**
     * 메일 전송
     * 보내는 사람 메일이 없으면 자동으로 지정
     * @param mail 메일 데이터
     */
    public void send(Mail mail) {
        if (mail.getFrom() == null) {
            mail.setFrom(mailConfig.getUsername());
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.addTo(mail.getTo());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getFrom(), mail.getSenderName()));
            mimeMessageHelper.setSubject(mail.getSubject());
            String body = mail.convertToString(templateEngine);
            mimeMessageHelper.setText(body, true);
            mimeMessageHelper.addInline("logo", new ClassPathResource("static/images/logo.png"));

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
