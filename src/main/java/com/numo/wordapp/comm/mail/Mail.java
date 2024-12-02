package com.numo.wordapp.comm.mail;

import lombok.Builder;
import lombok.Getter;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Getter
public class Mail {
    private final String to;
    private String from;
    private final String senderName;
    private final String subject;
    private final String body;
    private final TemplateContent templateContent;

    @Builder
    public Mail(String to, String from, String subject, String body, TemplateContent templateContent) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.templateContent = templateContent;
        this.senderName = "vocabox";
    }

    public String convertToString(SpringTemplateEngine templateEngine) {
        Context context = new Context();
        List<TemplateVariable> variables = templateContent.getVariables();

        for (TemplateVariable variable : variables) {
            context.setVariable(variable.getName(), variable.getValue());
        }

        return templateEngine.process(templateContent.getTemplate(), context);
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
