package com.numo.wordapp.util;

import com.numo.wordapp.conf.property.ApplicationContextProvider;
import com.numo.wordapp.conf.property.PropertyConfig;

public class ProcessBuilderUtil {
    private String program;
    private String gttsCommand = "gtts-cli '0_gttsText' -l ja --output 0_gttsFileName.mp3";
    private String fileName;
    private String path;
    String[] command = new String[3];

    public ProcessBuilderUtil(String fileName, String text){
        this.setEnvironment();
        this.fileName = fileName;
        // gtts 초기 텍스트 대체
        this.command[0] = this.program;
        this.command[2] = this.gttsCommand.replace("0_gttsText", text);    // 단어
        System.out.println(this.path + fileName);
        this.command[2] = this.command[2].replace("0_gttsFileName", path + fileName); // 파일이름
    }

    public int run(){
        int exitCode = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder(this.command);
            Process p = pb.start();
            exitCode = p.waitFor();
            p.destroy();
        }catch (Exception e){
        }
        return exitCode;
    }

    private void setEnvironment(){
        PropertyConfig propertyConfig = ApplicationContextProvider.getBean("propertyConfig",PropertyConfig.class);
        this.program = propertyConfig.getProgram();
        this.path = propertyConfig.getPath();
        if (propertyConfig.getProfile().equals("dev")){
            this.command[1] = "/c";
        }

        if (propertyConfig.getProfile().equals("env")){
            this.command[1] = "-c";
        }
    }
}
