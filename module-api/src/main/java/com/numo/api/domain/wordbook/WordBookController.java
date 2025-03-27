package com.numo.api.domain.wordbook;

import com.numo.api.domain.wordbook.dto.WordBookRequestDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.dto.WordBookUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/wordbooks")
@RestController
@RequiredArgsConstructor
public class WordBookController {
    private final WordBookService wordBookService;

    @Operation(description = "단어장 리스트를 가져온다.")
    @GetMapping
    public ResponseEntity<List<WordBookResponseDto>> getWordBooks(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(wordBookService.getWordBooks(user.getUserId()));
    }

    @Operation(description = "공유 단어장 리스트를 가져온다.")
    @GetMapping("/share")
    public ResponseEntity<List<WordBookResponseDto>> getShareWordBooks(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(wordBookService.getShareWordBooks(user.getUserId()));
    }

    @Operation(description = "단어장 정보를 가져온다.")
    @GetMapping("/{wordBookId}")
    public ResponseEntity<WordBookResponseDto> getWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                                           @PathVariable("wordBookId") Long wordBookId){
        return ResponseEntity.ok(wordBookService.getWordBook(user.getUserId(), wordBookId));
    }

    @Operation(description = "단어장을 생성한다.")
    @PostMapping
    public ResponseEntity<WordBookResponseDto> saveWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                                            @RequestBody WordBookRequestDto folderDto){
        return ResponseEntity.ok(wordBookService.saveWordBook(user.getUserId(), folderDto));
    }

    @Operation(description = "단어장을 변경한다.")
    @PutMapping (value = "/{wordBookId}")
    public ResponseEntity<WordBookResponseDto> updateFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                            @PathVariable("wordBookId") Long wordBookId,
                                                            @RequestBody WordBookUpdateDto folderDto){
        return ResponseEntity.ok(wordBookService.updateWordBook(user.getUserId(), wordBookId, folderDto));
    }

    @Operation(description = "단어장을 삭제한다.")
    @DeleteMapping(value="/{wordBookId}")
    public ResponseEntity<Integer> removeWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                                  @PathVariable("wordBookId") Long wordBookId){
        wordBookService.removeWordBook(user.getUserId(), wordBookId);
        return ResponseEntity.noContent().build();
    }
}
