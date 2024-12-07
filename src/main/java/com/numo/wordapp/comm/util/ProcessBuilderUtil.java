package com.numo.wordapp.comm.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * cli 커맨드 입력을 위한 유틸 클래스
 */
@Slf4j
public class ProcessBuilderUtil {

    private String gttsCommand;

    public ProcessBuilderUtil(String path, String text){
        this(path, text, "ja");
    }

    /**
     * 쉘스크립트를 실행시키기 위해 정보를 초기화한다.
     * @param path 환경 변수에 설정한 path
     * @param text 저장하길 원하는 text
     * @param type 발음 타입 (ja, en...)
     */
    public ProcessBuilderUtil(String path, String text, String type){
        if (type == null || type.isEmpty()) {
            type = "ja";
        }
        gttsCommand = """
                $_path/gtts_start.sh $_text $_type $_path
                """
                .replace("$_text", text)
                .replace("$_type", type)
                .replace("$_path", path);
    }

    /**
     * gtts 모듈로 발음 파일 생성
     * @return 0: 정상 종료  -1: 비정상 종료
     * */
    public int run() {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("sh", "-c", gttsCommand);
            Process p = pb.start();
            log(p);
            int exitCode = p.waitFor();
            p.destroy();
            return exitCode;
        } catch (Exception e){
            log.info(e.toString());
            return -1;
        }
    }

    /**
     * debug일 때만 로그 출력
     * @param p 프로세스 객체
     */
    private void log(Process p) {
        if (log.isDebugEnabled()) {
            log.debug("cli command = {}", gttsCommand);
            try {
                String s = "";
                StringBuilder sb = new StringBuilder();
                BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = stdError.readLine()) != null) {
                    sb.append(s);
                }
                log.debug("sb = {}", sb);
            } catch (Exception e) {
                log.error("로그를 출력하던 중 오류가 발생했습니다.");
            }
        }
    }

}
