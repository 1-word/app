package com.numo.api.comm.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TemplateVariable {
    private String name;
    private Object value;
}