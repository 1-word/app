package com.numo.wordapp.util;

import com.numo.wordapp.conf.property.ApplicationContextProvider;
import com.numo.wordapp.conf.property.PropertyConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessBuilderUtil {
    /**
     * type 기본값: ja(일본어)
     * ja: 일본어
     * en: 영어
     */
    private String program;
    private String type;
    private String gttsCommand = "gtts-cli \"0_gttsText\" -l 0_gttsType --output 0_gttsFileName.mp3";
    private String fileName;
    private String path;
    String[] command = new String[3];

    public ProcessBuilderUtil(String text, String fileName){
        this.setEnvironment();
        this.fileName = fileName;
        // gtts 초기 텍스트 대체
        this.command[0] = this.program;
        this.command[2] = this.gttsCommand.replace("0_gttsText", text);    // 단어
        log.info(this.path + fileName);
        this.command[2] = this.command[2].replace("0_gttsFileName", path + fileName); // 파일이름
        this.type = "ja";
    }

    public ProcessBuilderUtil(String text, String fileName, String type){
        this(text, fileName);
        this.type = type;
        this.command[2] = this.command[2].replace("0_gttsType", type);
    }

    /**
     * gtts 모듈로 발음 파일 생성
     * @return 0: 비정상 종료  1: 정상 종료
     * */
    public int run(){
        this.command[2] = this.command[2].replace("0_gttsType", this.type);
        log.info("file_path: {}", this.command[2]);
        int exitCode = 0;
        try {
            ProcessBuilder pb = new ProcessBuilder(this.command);
            Process p = pb.start();
            exitCode = p.waitFor();
            p.destroy();
        }catch (Exception e){
            log.info(e.toString());
        }
        return exitCode;
    }

    private void setEnvironment(){
        PropertyConfig propertyConfig = ApplicationContextProvider.getBean("propertyConfig", PropertyConfig.class);
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
