package com.numo.api.domain.wordbook.word;

import com.numo.api.domain.wordbook.word.dto.WordRequestDtoV2;
import com.numo.api.domain.wordbook.word.dto.WordResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordListResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordResponseDto;
import com.numo.api.domain.wordbook.word.dto.search.SearchWordRequestDto;
import com.numo.api.domain.wordbook.word.service.WordServiceV2;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.security.service.UserDetailsImpl;
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
@RequestMapping("/v2/words")
@RequiredArgsConstructor
public class WordControllerV2 {

    private final WordServiceV2 wordService;

    @Operation(summary = "단어 검색", description = "단어를 검색한다.")
    @GetMapping(value = "/search/{text}")
    public ResponseEntity<ReadWordListResponseDto> getSearchWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                                 @PathVariable("text") String searchText,
                                                                 PageRequestDto page,
                                                                 SearchWordRequestDto requestDto) {
        ReadWordRequestDto readDto = ReadWordRequestDto.of(searchText, requestDto);
        return ResponseEntity.ok(wordService.getWord(user.getUserId(), page, readDto));
    }

    @Operation(summary = "단어 조회", description = "단어를 조회한다.")
    @GetMapping
    public ResponseEntity<ReadWordListResponseDto> getWords(@AuthenticationPrincipal UserDetailsImpl user,
                                                            PageRequestDto page,
                                                            ReadWordRequestDto readDto) {
        return ResponseEntity.ok(wordService.getWord(user.getUserId(), page, readDto));
    }

    @Operation(summary = "단어 단건 조회", description = "해당하는 단어를 조회한다.")
    @GetMapping("/{wordId}")
    public ResponseEntity<ReadWordResponseDto> getWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                       @PathVariable("wordId") Long wordId) {
        return ResponseEntity.ok(wordService.getWord(user.getUserId(), wordId));
    }

    @Operation(summary = "단어 저장", description = "단어를 저장한다.")
    @PostMapping(value = "/{gttsType}")
    public ResponseEntity<WordResponseDto> saveWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                    @PathVariable("gttsType") GttsCode gttsType,
                                                    @Valid @RequestBody WordRequestDtoV2 dto) {
        return ResponseEntity.ok(wordService.saveWord(user.getUserId(), gttsType, dto));
    }

    @Operation(summary = "단어 수정", description = "타입에 맞는 단어를 수정한다.")
    @PutMapping(value = "/{wordId}/{type}")
    public ResponseEntity<WordResponseDto> updateWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                      @PathVariable("type") UpdateType type,
                                                      @PathVariable("wordId") Long wordId,
                                                      @RequestBody UpdateWordDto dto) {
        return ResponseEntity.ok(wordService.updateWord(user.getUserId(), wordId, dto, type));
    }

    @Operation(summary = "단어장 이동", description = "해당하는 단어의 단어장을 이동한다.")
    @PutMapping("/{wordId}/wordbook/{wordBookId}")
    public ResponseEntity<Void> moveWordBook(@AuthenticationPrincipal UserDetailsImpl user,
                                           @PathVariable("wordId") Long wordId,
                                           @PathVariable("wordBookId") Long wordBookId) {
        wordService.moveWordBook(user.getUserId(), wordId, wordBookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단어 삭제", description = "단어를 삭제한다.")
    @DeleteMapping(value = "/{wordId}")
    public ResponseEntity<Void> removeWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("wordId") Long wordId) {
        wordService.removeWord(user.getUserId(), wordId);
        return ResponseEntity.noContent().build();
    }

}
