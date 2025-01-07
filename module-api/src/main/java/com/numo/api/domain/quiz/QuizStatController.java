package com.numo.api.domain.quiz;

import com.numo.api.domain.quiz.dto.QuizStatResponseDto;
import com.numo.api.domain.quiz.service.QuizStatService;
import com.numo.api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/quiz-stat")
@RestController
@RequiredArgsConstructor
public class QuizStatController {
    private final QuizStatService quizStatService;

    @PostMapping("/{quizInfoId}")
    public ResponseEntity<QuizStatResponseDto> createQuizStat(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("quizInfoId") Long quizInfoId) {
        return ResponseEntity.ok(quizStatService.createQuizStat(quizInfoId, user.getUserId()));
    }

    @GetMapping("/{quizStatId}")
    public ResponseEntity<QuizStatResponseDto> getQuizStat(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("quizStatId") Long quizStatId) {
        return ResponseEntity.ok(quizStatService.getQuizStat(quizStatId, user.getUserId()));
    }

    @GetMapping
    public ResponseEntity<QuizStatResponseDto> getQuizStats(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(quizStatService.getQuizStats(user.getUserId()));
    }
}
