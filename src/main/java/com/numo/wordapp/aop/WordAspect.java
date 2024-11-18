package com.numo.wordapp.aop;

import com.numo.wordapp.comm.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP로 정의하는 클래스 지정
@Component  //빈 등록
public class WordAspect {
    JsonUtil jsonUtil = new JsonUtil();
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
    private void wordControllerCut(){}

    @Pointcut("execution(* com.numo.wordapp.controller..*.*(..))")
    private void securityControllerCut(){}

    /**
     * 1. /word로 들어오는 URL에 해당하는 파라미터 출력<br>
     * 2. 토큰의 ID를 파라미터의 가장 앞에 추가
     * @param joinPoint
     * @return Object ({@link com.numo.wordapp.controller.WordController}의 각 메서드의 파라미터 값)
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
                log.debug("[before method] args: " + jsonUtil.getJson(args));
            }
        }

        return joinPoint.proceed(args);
    }

}
