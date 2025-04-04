package com.numo.api.domain.wordbook.word;

import com.numo.api.domain.wordbook.aop.WordBookAccess;
import com.numo.api.domain.wordbook.word.dto.WordMoveDto;
import com.numo.api.domain.wordbook.word.dto.WordRequestDto;
import com.numo.api.domain.wordbook.word.dto.WordResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordResponseDto;
import com.numo.api.domain.wordbook.word.dto.search.SearchWordRequestDto;
import com.numo.api.domain.wordbook.word.service.WordService;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.domain.wordbook.WordBookRole;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.type.UpdateType;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/wordbooks/{wordBookId}/words")
@RequiredArgsConstructor
public class WordControllerV3 {

    private final WordService wordService;

    @Operation(summary = "단어 검색", description = "단어를 검색한다.")
    @GetMapping(value = "/search/{text}")
    @WordBookAccess(WordBookRole.view)
    public ResponseEntity<PageResponse<ReadWordResponseDto>> getSearchWord(@PathVariable("wordBookId") Long wordBookId,
                                                                           @PathVariable("text") String searchText,
                                                                           PageRequestDto page,
                                                                           SearchWordRequestDto requestDto) {
        ReadWordRequestDto readDto = ReadWordRequestDto.of(searchText, requestDto);
        return ResponseEntity.ok(wordService.getWord(wordBookId, page, readDto));
    }

    @Operation(summary = "단어 조회", description = "단어를 조회한다.")
    @GetMapping
    @WordBookAccess(WordBookRole.view)
    public ResponseEntity<PageResponse<ReadWordResponseDto>> getWords(@PathVariable("wordBookId") Long wordBookId,
                                                                      PageRequestDto page,
                                                                      ReadWordRequestDto readDto) {
        return ResponseEntity.ok(wordService.getWord(wordBookId, page, readDto));
    }

    @Operation(summary = "단어 단건 조회", description = "해당하는 단어를 조회한다.")
    @GetMapping("/{wordId}")
    @WordBookAccess(WordBookRole.view)
    public ResponseEntity<ReadWordResponseDto> getWord(@PathVariable("wordBookId") Long wordBookId,
                                                       @PathVariable("wordId") Long wordId) {
        return ResponseEntity.ok(wordService.getWord(wordId));
    }

    @Operation(summary = "단어 저장", description = "단어를 저장한다.")
    @PostMapping(value = "/{lang}")
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<WordResponseDto> saveWord(@PathVariable("wordBookId") Long wordBookId,
                                                    @AuthenticationPrincipal UserDetailsImpl user,
                                                    @PathVariable("lang") GttsCode gttsType,
                                                    @Valid @RequestBody WordRequestDto dto) {
        return ResponseEntity.ok(wordService.saveWord(user.getUserId(), wordBookId, gttsType, dto));
    }

    @Operation(summary = "단어 수정", description = "타입에 맞는 단어를 수정한다.")
    @PutMapping(value = "/{wordId}/{type}")
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<WordResponseDto> updateWord(@PathVariable("wordBookId") Long wordBookId,
                                                      @PathVariable("wordId") Long wordId,
                                                      @PathVariable("type") UpdateType type,
                                                      @RequestBody UpdateWordDto dto) {
        return ResponseEntity.ok(wordService.updateWord(wordId, dto, type));
    }

    @Operation(summary = "단어장 이동", description = "해당하는 단어의 단어장을 이동한다.")
    @PutMapping("/{wordId}/move")
    public ResponseEntity<Void> moveWordBook(@PathVariable("wordBookId") Long wordBookId,
                                             @AuthenticationPrincipal UserDetailsImpl user,
                                             @PathVariable("wordId") Long wordId,
                                             @Valid @RequestBody WordMoveDto moveDto) {
        wordService.moveWordBook(user.getUserId(), moveDto.targetWordBookId(), wordId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단어 삭제", description = "단어를 삭제한다.")
    @DeleteMapping(value = "/{wordId}")
    @WordBookAccess(WordBookRole.edit)
    public ResponseEntity<Void> removeWord(@PathVariable("wordBookId") Long wordBookId,
                                           @PathVariable("wordId") Long wordId) {
        wordService.removeWord(wordId);
        return ResponseEntity.noContent().build();
    }

}
