package com.numo.api.domain.quiz;

import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoRequestDto;
import com.numo.api.domain.quiz.dto.quizInfo.QuizInfoResponseDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.domain.quiz.service.QuizInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/quiz-info")
@RestController
@RequiredArgsConstructor
public class QuizInfoController {
    private final QuizInfoService quizInfoService;

    @Operation(summary = "퀴즈 정보 생성")
    @PostMapping
    public ResponseEntity<Long> createQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @RequestBody QuizInfoRequestDto requestDto) {
        return ResponseEntity.ok(quizInfoService.createQuizInfo(user.getUserId(), requestDto));
    }

    @Operation(summary = "최신에 완료하지 못한 퀴즈 정보 조회")
    @GetMapping("/incomplete")
    public ResponseEntity<QuizInfoResponseDto> getInCompleteQuizInfo(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(quizInfoService.getInCompleteQuizInfo(user.getUserId()));
    }

    @Operation(summary = "퀴즈 정보 조회")
    @GetMapping("/{quizInfoId}")
    public ResponseEntity<QuizInfoResponseDto> getQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @PathVariable("quizInfoId") Long quizInfoId) {
        return ResponseEntity.ok(quizInfoService.getQuizInfo(user.getUserId(), quizInfoId));
    }

    @Operation(summary = "퀴즈 정보 삭제.", description = "삭제 시 연동된 퀴즈 데이터까지 모두 삭제(통계 포함)")
    @DeleteMapping("/{quizInfoId}")
    public ResponseEntity<Void> deleteQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @PathVariable("quizInfoId") Long quizInfoId) {
        quizInfoService.deleteQuizInfo(user.getUserId(), quizInfoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "퀴즈 완료")
    @PutMapping("/complete/{quizInfoId}")
    public ResponseEntity<Void> completeQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                             @PathVariable("quizInfoId") Long quizInfoId) {
        quizInfoService.completeQuiz(user.getUserId(), quizInfoId);
        return ResponseEntity.noContent().build();
    }
}
