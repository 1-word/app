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

    @Before("@annotation(annotation) && args(wordBookId, ..)")
    public void checkPermission(WordBookAccess annotation, Long wordBookId) {
        Long userId = getSessionUser();
        WordBookRole role = annotation.value();

        if (!wordBookService.isOwner(userId, wordBookId)) {
            wordBookMemberService.checkPermission(userId, wordBookId, role);
        }
    }

    private Long getSessionUser() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetailsImpl.getUserId();
    }

}
