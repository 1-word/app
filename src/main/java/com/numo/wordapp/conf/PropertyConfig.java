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
    private String processPath;
    @Value("${cstm.file.path}")
    private String storagePath;
    @Value("${CLIENT_HOST}")
    private String clientHost;

    public String getProcessPath() {
        if (processPath.endsWith("/")) {
            return processPath.substring(0, processPath.length() - 2);
        }
        return processPath;
    }

    public String getPathWithEndFileSeparation() {
        if (!storagePath.endsWith("/")) {
            storagePath += "/";
        }
        return storagePath;
    }
}
