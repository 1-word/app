package com.numo.api.domain.quiz;

import com.numo.api.domain.quiz.dto.QuizStatResponseDto;
import com.numo.api.domain.quiz.service.QuizStatService;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<QuizStatResponseDto>> getQuizStats(@AuthenticationPrincipal UserDetailsImpl user,
                                                                  DateRequestDto dateRequest) {
        return ResponseEntity.ok(quizStatService.getQuizStatList(user.getUserId(), dateRequest));
    }

    @DeleteMapping("/{quizStatId}")
    public ResponseEntity<Void> deleteQuizStat(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable("quizStatId") Long quizStatId) {
        quizStatService.deleteQuizStat(user.getUserId(), quizStatId);
        return ResponseEntity.noContent().build();
    }
}
