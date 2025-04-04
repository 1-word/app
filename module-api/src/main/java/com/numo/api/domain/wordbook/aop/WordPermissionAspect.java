package com.numo.api.domain.wordbook.aop;

import com.numo.api.domain.wordbook.service.WordBookMemberService;
import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.WordBookRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class WordPermissionAspect {
    private final WordBookService wordBookService;
    private final WordBookMemberService wordBookMemberService;

    /**
     * 단어장의 권한을 체크한다.
     * 반드시 첫 번째 매개변수가 wordBookId 이어야 실행된다.
     *
     * 전체 사용자 권한을 체크하고 그 다음에 멤버 권한을 체크한다.
     * 만약 전체 사용자 권한이 멤버 권한보다 높다면, 전체 사용자 권한을 따라간다.
     * @param annotation
     * @param wordBookId 단어장
     */
    @Before("@annotation(annotation) && args(wordBookId, ..)")
    public void checkPermission(WordBookAccess annotation, Long wordBookId) {
        Long userId = getSessionUser();
        WordBookRole targetRole = annotation.value();

        if (!wordBookService.hasPermission(userId, wordBookId, targetRole)) {
            wordBookMemberService.checkPermission(userId, wordBookId, targetRole);
        }
    }

    private Long getSessionUser() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsImpl.getUserId();
    }

}
