package com.numo.api.domain.quiz;

import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.domain.quiz.dto.QuizQuestionDto;
import com.numo.api.domain.quiz.dto.QuizResponseDto;
import com.numo.api.domain.quiz.dto.QuizSolvedRequestDto;
import com.numo.api.domain.quiz.dto.QuizSolvedListRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.domain.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/quiz")
@RestController
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @Operation(summary = "퀴즈 생성", description = "폴더에 맞는 전체 단어 데이터를 응답")
    @PostMapping("/{quizInfoId}")
    public ResponseEntity<List<QuizQuestionDto>> createQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                                            @PathVariable("quizInfoId") Long quizInfoId) {
        return ResponseEntity.ok(quizService.createQuiz(user.getUserId(), quizInfoId));
    }

    @Operation(summary = "퀴즈 조회", description = "퀴즈 문제 데이터 조회")
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

    @Operation(summary = "퀴즈 이어하기")
    @GetMapping("/continue/{quizInfoId}")
    public ResponseEntity<PageResponse<QuizResponseDto>> continueQuiz(@AuthenticationPrincipal UserDetailsImpl user,
                                                                     @PathVariable("quizInfoId") Long quizInfoId,
                                                                     PageRequestDto pageDto) {
        return ResponseEntity.ok(quizService.getUnsolvedQuiz(user.getUserId(), quizInfoId, pageDto));
    }
}
