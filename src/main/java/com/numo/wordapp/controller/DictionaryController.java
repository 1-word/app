package com.numo.wordapp.controller;

import com.numo.wordapp.dto.dictionary.DictionaryDto;
import com.numo.wordapp.service.dictionary.DictionaryService;
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
    @GetMapping("/{searchText}")
    public ResponseEntity<List<DictionaryDto>> searchWord(@PathVariable("searchText") String searchText) {
        return ResponseEntity.ok(dictionaryService.search(searchText));
    }
}
