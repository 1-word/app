package com.numo.api.controller;

import com.numo.api.comm.page.PageResponse;
import com.numo.api.dto.page.PageRequestDto;
import com.numo.api.dto.quiz.QuizResponseDto;
import com.numo.api.dto.quiz.QuizSolvedRequestDto;
import com.numo.api.dto.quiz.QuizSolvedListRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.service.quiz.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/quiz")
@RestController
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @Operation(summary = "퀴즈를 생성한다.")
    @PostMapping("/{quizInfoId}")
    public ResponseEntity<Void> createQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                                                    @PathVariable("quizInfoId") Long quizInfoId) {
        quizService.createQuiz(user.getUserId(), quizInfoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "퀴즈를 조회한다.")
    @GetMapping("/{quizInfoId}")
    public ResponseEntity<PageResponse<QuizResponseDto>> getQuizInfo(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("quizInfoId") Long quizInfoId,
                                                           PageRequestDto pageDto) {
        return ResponseEntity.ok(quizService.getQuizInfo(user.getUserId(), quizInfoId, pageDto));
    }

    @Operation(summary = "퀴즈 단건 풀이")
    @PutMapping("/{quizId}")
    public ResponseEntity<Void> solveQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                                                   @PathVariable("quizId") Long quizId,
                                                                   @RequestBody QuizSolvedRequestDto requestDto) {
        quizService.solveQuiz(user.getUserId(), quizId, requestDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "퀴즈 다건 풀이")
    @PutMapping
    public ResponseEntity<Void> solveQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                          @RequestBody QuizSolvedListRequestDto requestDto) {
        quizService.solveQuizzes(user.getUserId(), requestDto.datas());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "퀴즈를 이어한다.")
    @GetMapping("/continue/{quizInfoId}")
    public ResponseEntity<PageResponse<QuizResponseDto>> continueQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                                                     @PathVariable("quizInfoId") Long quizInfoId,
                                                                     PageRequestDto pageDto) {
        return ResponseEntity.ok(quizService.getUnsolvedQuiz(user.getUserId(), quizInfoId, pageDto));
    }
}
