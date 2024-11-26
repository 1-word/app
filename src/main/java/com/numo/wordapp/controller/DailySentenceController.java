package com.numo.wordapp.controller;

import com.numo.wordapp.dto.sentence.DailySentenceRequestDto;
import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.sentence.DailySentenceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
