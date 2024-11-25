package com.numo.wordapp.aop;

import com.numo.wordapp.comm.util.JsonUtil;
import com.numo.wordapp.dto.dictionary.DictionaryDto;
import com.numo.wordapp.dto.word.WordResponseDto;
import com.numo.wordapp.service.dictionary.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP로 정의하는 클래스 지정
@Component  //빈 등록
public class WordAspect {

    private final DictionaryService dictionaryService;

    public WordAspect(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @Pointcut("execution(* com.numo.wordapp.controller..*.*(..))")
    private void wordControllerCut(){}

    @Pointcut("execution(* com.numo.wordapp.controller..*.*(..))")
    private void securityControllerCut(){}

    /**
     * request URL에 해당하는 파라미터 출력<br>
     * */
    @Around("wordControllerCut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        String cls = joinPoint.getSignature().getDeclaringTypeName();
        //실행되는 함수 이름 읽어옴
        String method = joinPoint.getSignature().getName();

        //메서드 매개변수 배열 읽어옴
        Object[] args = joinPoint.getArgs();

        log.info("[WordAdvice before]: {} {}()", cls, method);
        if (log.isDebugEnabled()) {
            if (args.length != 0) {
                JsonUtil jsonUtil = new JsonUtil();
                log.debug("[before method] args: " + jsonUtil.getJson(args));
            }
        }

        return joinPoint.proceed(args);
    }

    /**
     * 단어 저장 시 사전 데이터베이스에 저장
     * 이미 사전 데이터베이스에 있다면 저장하지 않는다.
     * @param res 저장된 단어 데이터
     */
    @AfterReturning(value = "execution(* com.numo.wordapp.service..WordService.saveWord(..))", returning = "res")
    public void afterWordSave(WordResponseDto res) {
        DictionaryDto dictionaryDto = DictionaryDto.builder()
                .word(res.word())
                .build();

        dictionaryService.save(dictionaryDto);
    }

}
