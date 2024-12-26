package com.numo.api.controller;

import com.numo.api.dto.quiz.QuizInfoRequestDto;
import com.numo.api.dto.quiz.QuizInfoResponseDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.service.quiz.QuizInfoService;
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

    @Operation(summary = "퀴즈 정보를 생성한다.")
    @PostMapping
    public ResponseEntity<Long> createQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @RequestBody QuizInfoRequestDto requestDto) {
        return ResponseEntity.ok(quizInfoService.createQuizInfo(user.getUserId(), requestDto));
    }

    @Operation(summary = "퀴즈 정보를 조회한다.")
    @GetMapping("{quizInfoId}")
    public ResponseEntity<QuizInfoResponseDto> getQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @PathVariable("quizInfoId") Long quizInfoId) {
        return ResponseEntity.ok(quizInfoService.getQuizInfo(user.getUserId(), quizInfoId));
    }

    @Operation(summary = "퀴즈 정보를 삭제한다.", description = "삭제 시 연동된 퀴즈 데이터까지 모두 삭제(통계 포함)")
    @DeleteMapping("{quizInfoId}")
    public ResponseEntity<Void> deleteQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                           @PathVariable("quizInfoId") Long quizInfoId) {
        quizInfoService.deleteQuizInfo(user.getUserId(), quizInfoId);
        return ResponseEntity.noContent().build();
    }
}
