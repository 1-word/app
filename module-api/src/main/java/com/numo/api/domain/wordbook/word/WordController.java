package com.numo.api.domain.wordbook.word;

import com.numo.domain.word.dto.UpdateWordDto;
import com.numo.domain.word.type.UpdateType;
import com.numo.api.domain.wordbook.word.aop.WordAspect;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.domain.wordbook.word.dto.WordRequestDto;
import com.numo.api.domain.wordbook.word.dto.WordResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordListResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordResponseDto;
import com.numo.api.domain.wordbook.word.dto.search.SearchWordRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.domain.wordbook.word.service.WordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * {web}/word에서 요청하는 CRUD 기능
 *
 * @apiNote userId는 {@link WordAspect} before()메서드에서 토큰의 id를 확인하여 파라미터의 가장 앞에 추가됨
 */

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @Operation(summary = "단어 검색", description = "단어를 검색한다.")
    @GetMapping(value = "/{text}")
    public ResponseEntity<ReadWordListResponseDto> getSearchWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                                 @PathVariable("text") String searchText,
                                                                 PageRequestDto page,
                                                                 SearchWordRequestDto requestDto) {
        Long userId = user.getUserId();
        ReadWordRequestDto readDto = ReadWordRequestDto.of(searchText, requestDto);
        return ResponseEntity.ok(wordService.getWord(userId, page, readDto));
    }

    @Operation(summary = "단어 조회", description = "단어를 조회한다.")
    @GetMapping
    public ResponseEntity<ReadWordListResponseDto> getWords(@AuthenticationPrincipal UserDetailsImpl user,
                                                            PageRequestDto page,
                                                            ReadWordRequestDto readDto) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordService.getWord(userId, page, readDto));
    }

    @Operation(summary = "단어 단건 조회", description = "해당하는 단어를 조회한다.")
    @GetMapping("/item/{wordId}")
    public ResponseEntity<ReadWordResponseDto> getWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                       @PathVariable("wordId") Long wordId) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordService.getWord(userId, wordId));
    }

    @Operation(summary = "단어 저장", description = "단어를 저장한다.")
    @PostMapping(value = "/{gttsType}")
    public ResponseEntity<WordResponseDto> saveWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                    @PathVariable("gttsType") String gttsType,
                                                    @RequestBody WordRequestDto dto) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordService.saveWord(userId, gttsType, dto));
    }

    @Operation(summary = "단어 수정", description = "타입에 맞는 단어를 수정한다.")
    @PutMapping(value = "/{type}/{wordId}")
    public ResponseEntity<WordResponseDto> updateWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                      @PathVariable("type") UpdateType type,
                                                      @PathVariable("wordId") Long wordId,
                                                      @RequestBody UpdateWordDto dto) {
        return ResponseEntity.ok(wordService.updateWord(user.getUserId(), wordId, dto, type));
    }

    @Operation(summary = "단어장 이동", description = "해당하는 단어의 단어장(폴더)을 이동한다.")
    @PutMapping("/{wordId}/folder/{folderId}")
    public ResponseEntity<Void> moveFolder(@AuthenticationPrincipal UserDetailsImpl user,
                                                     @PathVariable("wordId") Long wordId,
                                                     @PathVariable("folderId") Long folderId) {
        wordService.moveFolder(user.getUserId(), wordId, folderId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단어 삭제", description = "단어를 삭제한다.")
    @DeleteMapping(value = "/{wordId}")
    public ResponseEntity<Void> removeWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("wordId") Long wordId) {
        Long userId = user.getUserId();
        wordService.removeWord(userId, wordId);
        return ResponseEntity.noContent().build();
    }


}