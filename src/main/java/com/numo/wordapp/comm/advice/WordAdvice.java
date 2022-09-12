package com.numo.wordapp.comm.advice;

import com.numo.wordapp.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP로 정의하는 클래스 지정
@Component  //빈 등록
public class WordAdvice {
    // @Slf4j가 있으면 해당 역할을 대신 한다.
    // private final Logger log = LoggerFactory.getLogger(getClass());

    //조인포인트: 클라이언트가 호출하는 모든 비즈니스 메소드 (포인트컷 대상, 포인트컷 후보)
    //포인트컷: 필터링된 조인포인트
        //표현식 => 리턴타입 패키지경로..*클래스명*메소드명*(..)
        // ex) * com.numo.wordapp.controller..*.*(..)   => 컨트롤러의 모든 메소드 포인트컷..
    //어드바이스: 공통 기능 코드(로그, 예외처리 등)
        //Before: 조인포인트 호출 전 실행되는 코드 (코드 실행 자체 관여할 수 X)
        //After Returning: 모든 실행이 정상적으로 이루어진 후 동작
        //After Throwing: 예외발생한 뒤 동작
        //After: 메소드 실행 후 (정상 실행, 예외 발생 구분 없음)
        //Around: 메소드 실행 제어(직접 대상 메소드 호출)

    //포인트컷 정의   컨트롤러 패키지 하위 클래스 전부 적용
    @Pointcut("execution(* com.numo.wordapp.controller..*.*(..))")
    private void cut(){}

    @Before("cut()")
    public void before(JoinPoint joinPoint){

        String cls = joinPoint.getSignature().getDeclaringTypeName();
        //실행되는 함수 이름 읽어옴
        String method = joinPoint.getSignature().getName();

        //메서드 매개변수 배열 읽어옴
        Object[] args = joinPoint.getArgs();

        //System.out.println("[before method] method: " + method);
        //log.info("[before method] method: " + method + "()");
        log.info("[WordAdvice before]: {} {}()", cls, method);
        if (args.length != 0) {
            JsonUtil jsonUtil = new JsonUtil(args);
            //System.out.println("[before method] args: " + jsonUtil.getJson());
            log.info("[before method] args: " + jsonUtil.getJson());
        }
    }

}
