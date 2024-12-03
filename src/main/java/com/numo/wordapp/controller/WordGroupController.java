package com.numo.wordapp.controller;

import com.numo.wordapp.dto.word.group.WordGroupRequestDto;
import com.numo.wordapp.dto.word.group.WordGroupResponseDto;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.word.WordGroupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/wordGroup")
@RestController
@RequiredArgsConstructor
public class WordGroupController {
    private final WordGroupService wordGroupService;

    @Operation(description = "품사 리스트를 조회한다.")
    @GetMapping
    public ResponseEntity<List<WordGroupResponseDto>> getWordGroupList(@AuthenticationPrincipal UserDetailsImpl user) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordGroupService.getWordGroupList(userId));
    }

    @Operation(description = "품사를 저장한다.")
    @PostMapping
    public ResponseEntity<WordGroupResponseDto> saveWordGroup(@AuthenticationPrincipal UserDetailsImpl user,
                                                              @RequestBody WordGroupRequestDto requestDto) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordGroupService.saveWordGroup(userId, requestDto));
    }

    @Operation(description = "품사를 수정한다.")
    @PutMapping("/{wordGroupId}")
    public ResponseEntity<WordGroupResponseDto> updateWordGroup(@AuthenticationPrincipal UserDetailsImpl user,
                                                              @PathVariable("wordGroupId") Long wordGroupId,
                                                              @RequestBody WordGroupRequestDto requestDto) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordGroupService.updateWordGroup(userId, wordGroupId, requestDto));
    }

    @Operation(description = "품사를 삭제한다.")
    @DeleteMapping("/{wordGroupId}")
    public ResponseEntity<Void> removeWordGroup(@AuthenticationPrincipal UserDetailsImpl user,
                                                @PathVariable("wordGroupId") Long wordGroupId) {
        Long userId = user.getUserId();
        wordGroupService.removeWordGroup(userId, wordGroupId);
        return ResponseEntity.noContent().build();
    }
}
