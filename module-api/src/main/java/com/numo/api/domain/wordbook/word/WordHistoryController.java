package com.numo.api.domain.wordbook.word;

import com.numo.api.domain.wordbook.aop.WordBookAccess;
import com.numo.api.domain.wordbook.word.dto.WordHistoryDto;
import com.numo.api.domain.wordbook.word.service.WordHistoryService;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.WordBookRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wordbooks/{wordBookId}/histories")
public class WordHistoryController {
    private final WordHistoryService wordHistoryService;

    @GetMapping
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<List<WordHistoryDto>> getWordHistoryByWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                                                         @PathVariable("wordBookId") Long wordBookId) {
        return ResponseEntity.ok(wordHistoryService.getWordHistoryByWordBook(wordBookId));
    }

    @PutMapping("/{wordHistoryId}/restore")
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<Void> restoreWord(@AuthenticationPrincipal UserDetailsImpl user,
                                            @PathVariable("wordHistoryId") Long wordHistoryId) {
        wordHistoryService.restore(user.getUserId(), wordHistoryId);
        return ResponseEntity.noContent().build();
    }
}
