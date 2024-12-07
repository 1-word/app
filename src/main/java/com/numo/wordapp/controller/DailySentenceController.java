package com.numo.wordapp.controller;

import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.dto.sentence.DailySentenceParameterDto;
import com.numo.wordapp.dto.sentence.DailySentenceRequestDto;
import com.numo.wordapp.dto.sentence.ReadDailySentenceDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.sentence.DailySentenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("daily-sentence")
@RestController
@RequiredArgsConstructor
public class DailySentenceController {
    private final DailySentenceService dailySentenceService;

    @Operation(summary = "오늘의 문장 저장", description = "오늘의 문장을 저장한다")
    @PostMapping
    public ResponseEntity<DailySentenceDto> saveSentence(@AuthenticationPrincipal UserDetailsImpl user,
                                                         @RequestBody DailySentenceRequestDto requestDto) {
        return ResponseEntity.ok(dailySentenceService.saveSentence(user.getUserId(), requestDto));
    }

    @Operation(summary = "오늘의 문장 조회", description = "오늘의 문장을 조회한다")
    @GetMapping
    public ResponseEntity<List<ReadDailySentenceDto>> getSentence(@AuthenticationPrincipal UserDetailsImpl user,
                                                                  DailySentenceParameterDto parameterDto) {
        return ResponseEntity.ok(dailySentenceService.getSentenceBy(user.getUserId(), parameterDto));
    }

    @Operation(summary = "오늘의 문장 날짜 리스트 조회", description = "년도, 월에 해당하는 저장된 오늘의 문장 데이터의 날짜를 계산한다.")
    @GetMapping("/days")
    public ResponseEntity<List<Integer>> getSentenceDaysByYearAndMonth(@AuthenticationPrincipal UserDetailsImpl user,
                                                                 DailySentenceParameterDto parameterDto) {
       return ResponseEntity.ok(dailySentenceService.getSentenceDaysByYearAndMonth(user.getUserId(), parameterDto));
    }

    @Operation(summary = "오늘의 문장 수정", description = "오늘의 문장을 수정한다")
    @PutMapping("/{dailySentenceId}")
    public ResponseEntity<DailySentenceDto> updateSentence(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("dailySentenceId") Long dailySentenceId,
                                                           @RequestBody DailySentenceRequestDto requestDto) {
        return ResponseEntity.ok(dailySentenceService.updateSentence(user.getUserId(), dailySentenceId, requestDto));
    }

    @Operation(summary = "오늘의 문장 삭제", description = "오늘의 문장을 삭제한다")
    @DeleteMapping("/{dailySentenceId}")
    public ResponseEntity<Void> deleteSentence(@AuthenticationPrincipal UserDetailsImpl user,
                                               @PathVariable("dailySentenceId") Long dailySentenceId) {
        dailySentenceService.deleteSentence(user.getUserId(), dailySentenceId);
        return ResponseEntity.noContent().build();
    }
}
