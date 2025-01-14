package com.numo.api.global.comm.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TemplateContent {
    private String template;
    private List<TemplateVariable> variables;
}
