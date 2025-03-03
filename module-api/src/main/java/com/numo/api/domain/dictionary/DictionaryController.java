package com.numo.api.domain.dictionary;

import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.api.domain.dictionary.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/dict")
@RestController
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @Operation(summary = "사전 검색", description = "사전을 검색한다.")
    @GetMapping("/list/{searchText}")
    public ResponseEntity<List<DictionaryDto>> searchWordList(@PathVariable("searchText") String searchText) {
        return ResponseEntity.ok(dictionaryService.searchWordList(searchText));
    }

    @Operation(summary = "사전 검색", description = "사전을 검색한다.")
    @GetMapping("/{searchText}")
    public ResponseEntity<DictionaryDto> searchWord(@PathVariable("searchText") String searchText) {
        return ResponseEntity.ok(dictionaryService.searchWord(searchText));
    }

    @Operation(summary = "자동 완성 사전 검색", description = "사전을 검색한다.")
    @GetMapping("/list/db/{searchText}")
    public ResponseEntity<List<DictionaryDto>> searchWordListDB(@PathVariable("searchText") String searchText) {
        return ResponseEntity.ok(dictionaryService.searchWordListDB(searchText));
    }
}
