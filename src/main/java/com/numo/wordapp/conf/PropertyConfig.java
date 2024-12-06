package com.numo.wordapp.conf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
    @Value("${CORS_URL}")
    private String corsUrl;

    public String getProcessPath() {
        if (processPath.endsWith("/")) {
            return processPath.substring(0, processPath.length() - 2);
        }
        return processPath;
    }

    public String getPathWithEndFileSeparation() {
        if (storagePath.endsWith("/")) {
            return storagePath.substring(0, storagePath.length()-1);
        }
        return storagePath;
    }

    public String[] getCorsUrl() {
        return corsUrl.split(",");
    }
}
