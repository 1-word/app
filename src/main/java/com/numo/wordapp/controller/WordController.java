package com.numo.wordapp.controller;

import com.numo.wordapp.aop.WordAspect;
import com.numo.wordapp.dto.word.*;
import com.numo.wordapp.security.service.UserDetailsImpl;
import com.numo.wordapp.service.word.WordService;
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
//    private final FolderService folderService;

    @Operation(description = "단어를 검색한다.")
    @GetMapping(value = "/{text}")
    public ResponseEntity<ReadWordResponseDto> getSearchWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                             @PathVariable("text") String searchText,
                                                             SearchWordRequestDto requestDto) {
        Long userId = user.getUserId();
        ReadWordRequestDto readDto = ReadWordRequestDto.of(searchText, requestDto);
        return ResponseEntity.ok(wordService.getWord(userId, readDto));
    }

    @Operation(description = "단어를 조회한다.")
    @GetMapping
    public ResponseEntity<ReadWordResponseDto> getWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                       SearchWordRequestDto requestDto) {
        Long userId = user.getUserId();
        ReadWordRequestDto readDto = ReadWordRequestDto.of(requestDto);
        return ResponseEntity.ok(wordService.getWord(userId, readDto));
    }

    @Operation(description = "단어를 저장한다.")
    @PostMapping(value = "/{gttsType}")
    public ResponseEntity<WordResponseDto> saveWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                    @PathVariable("gttsType") String gttsType,
                                                    @RequestBody WordRequestDto dto) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(wordService.saveWord(userId, gttsType, dto));
    }

    /**
     * 타입별 단어 데이터 업데이트
     * <pre>all: 모든 단어 데이터 업데이트</pre>
     * <pre>memorization: 암기 여부 업데이트</pre>
     * <pre>memo: 단어 메모 업데이트</pre>
     * <pre>wordFoler: 단어 폴더 변경</pre>
     *
     * @param userId 로그인한 유저 아이디
     * @param type   {@link WordService.UpdateType} all, memorization, memo, wordFoler
     * @param id     업데이트할 단어 아이디
     * @param dto    업데이트할 데이터
     * @return 업데이트한 단어 전체 데이터
     */
    @PutMapping(value = "/{type}/{wordId}")
    public ResponseEntity<WordResponseDto> updateWord(@AuthenticationPrincipal UserDetailsImpl user,
                                                      @PathVariable("type") String type,
                                                      @PathVariable("wordId") Long wordId,
                                                      @RequestBody UpdateWordDto dto) {
        return ResponseEntity.ok(wordService.updateByWord(user.getUserId(), wordId, dto, type));
    }

    @PutMapping("/{wordId}/folder/{folderId}")
    public ResponseEntity<String> updateFolderToWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable Long folderId) {
        return null;
    }

    @DeleteMapping(value = "/{wordId}")
    public ResponseEntity<Void> removeWord(@AuthenticationPrincipal UserDetailsImpl user, @PathVariable("wordId") Long wordId) {
        Long userId = user.getUserId();
        wordService.removeWord(userId, wordId);
        return ResponseEntity.noContent().build();
    }


}
