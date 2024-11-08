package com.numo.wordapp.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertyConfig {
    @Value("${cstm.processbuilder.program}")
    private String program;
    @Value("${cstm.processbuilder.path}")
    private String path;
}
