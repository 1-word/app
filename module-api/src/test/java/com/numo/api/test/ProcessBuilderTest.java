package com.numo.api.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
public class ProcessBuilderTest {
    @Autowired
    public Config config;
    @Value("${spring.processbuilder.program: cmd.exe}")
    private String program;
    private String gttsCommand = "gtts-cli '0_gttsText' -l ja --output 0_gttsFileName.mp3";
    private String fileName = "";
    String[] command = new String[] {program, "/c", ""};


    ProcessBuilderTest(int wordId, String text){
        System.out.println(program);
        this.fileName = wordId + "_" + text;
        // gtts 초기 텍스트 대체
        this.command[2] = this.gttsCommand.replace("0_gttsText", text);    // 단어
        this.command[2] = this.command[2].replace("0_gttsFileName", fileName); // 파일이름
    }

    public void run(){
        try {
            ProcessBuilder pb = new ProcessBuilder(this.command);
            Process p = pb.start();
            int exitCode = p.waitFor();
            System.out.println(exitCode);
            p.destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Component
    public static class Config{
        String program;

        public String getProgram() {
            return this.program;
        }
    }

    public static void main(String[] args) {
//        String text = "とぼけとぼけとぼけ";
//        new ProcessBuilderTest(1, text).run();
        new ProcessBuilderTest.Config();
        System.out.println(new ProcessBuilderTest.Config().getProgram());
    }
}
