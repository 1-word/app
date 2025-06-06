package com.numo.api.domain.wordbook;

import com.numo.api.domain.wordbook.aop.WordBookAccess;
import com.numo.api.domain.wordbook.dto.ShareWordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookRequestDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookSettingDto;
import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.WordBookRole;
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
    public ResponseEntity<List<ShareWordBookResponseDto>> getShareWordBooks(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(wordBookService.getShareWordBooks(user.getUserId()));
    }

    @Operation(description = "단어장을 생성한다.")
    @PostMapping
    public ResponseEntity<WordBookResponseDto> saveWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                                            @RequestBody WordBookRequestDto folderDto){
        return ResponseEntity.ok(wordBookService.saveWordBook(user.getUserId(), folderDto));
    }

    @Operation(description = "단어장 정보를 가져온다.")
    @GetMapping("/{wordBookId}")
    @WordBookAccess(WordBookRole.view)
    public ResponseEntity<WordBookResponseDto> getWordBook(@PathVariable("wordBookId") Long wordBookId) {
        return ResponseEntity.ok(wordBookService.getWordBook(wordBookId));
    }

    @Operation(description = "단어장을 변경한다.")
    @PutMapping (value = "/{wordBookId}")
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<WordBookResponseDto> updateFolder(@PathVariable("wordBookId") Long wordBookId,
                                                            @RequestBody WordBookUpdateDto folderDto){
        return ResponseEntity.ok(wordBookService.updateWordBook(wordBookId, folderDto));
    }

    @Operation(description = "단어장을 삭제한다.")
    @DeleteMapping(value="/{wordBookId}")
    @WordBookAccess(WordBookRole.admin)
    public ResponseEntity<Integer> removeWordBook(@PathVariable("wordBookId") Long wordBookId,
                                                  @AuthenticationPrincipal UserDetailsImpl user,
                                                  @RequestParam(value = "removeWords", required = false) Boolean removeWords){
        if (removeWords == null) {
            removeWords = false;
        }
        wordBookService.removeWordBook(wordBookId, removeWords);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단어장 설정 조회", description = "단어장 설정 조회, admin만 가능")
    @GetMapping("/{wordBookId}/setting")
    @WordBookAccess
    public ResponseEntity<WordBookSettingDto> getWordBookSetting(@PathVariable("wordBookId") Long wordBookId) {
        return ResponseEntity.ok(wordBookService.getWordBookSetting(wordBookId));
    }

    @Operation(summary = "단어장 설정 변경", description = "단어장 설정 변경, admin만 가능")
    @PutMapping("/{wordBookId}/setting")
    @WordBookAccess
    public ResponseEntity<Void> updateWordBookSetting(@PathVariable("wordBookId") Long wordBookId,
                                                      @RequestBody WordBookSettingDto settingDto) {
        wordBookService.updateWordBookSetting(wordBookId, settingDto);
        return ResponseEntity.noContent().build();
    }
}
