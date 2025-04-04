package com.numo.api.domain.wordbook;

import com.numo.api.domain.wordbook.dto.WordBookMemberResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookRoleRequestDto;
import com.numo.api.domain.wordbook.service.WordBookMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wordbooks/{wordBookId}/members")
public class WordBookMemberController {
    private final WordBookMemberService wordBookMemberService;

    @Operation(description = "단어장의 멤버를 조회한다")
    @GetMapping
    public ResponseEntity<List<WordBookMemberResponseDto>> getWordBookMembers(@PathVariable("wordBookId") Long wordBookId) {
        return ResponseEntity.ok(wordBookMemberService.getWordBookMembers(wordBookId));
    }

    @Operation(description = "단어장의 멤버를 추가한다")
    @PostMapping
    public ResponseEntity<Void> createWordBookUserRole(@PathVariable("wordBookId") Long wordBookId,
                                                       @RequestBody WordBookRoleRequestDto roleDto) {
        wordBookMemberService.addWordBookMember(wordBookId, roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(description = "단어장의 권한을 변경한다.")
    @PutMapping(value = "/role")
    public ResponseEntity<Void> updateWordBookMemberRole(@PathVariable("wordBookId") Long wordBookId,
                                                         @RequestBody WordBookRoleRequestDto roleDto) {
        wordBookMemberService.updateWordBookMemberRole(wordBookId, roleDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "단어장의 멤버를 삭제한다.")
    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> deleteWordBookMemberRole(@PathVariable("wordBookId") Long wordBookId,
                                                         @PathVariable("memberId") Long memberId) {
        wordBookMemberService.deleteWordBookMemberRole(wordBookId, memberId);
        return ResponseEntity.noContent().build();
    }

}
