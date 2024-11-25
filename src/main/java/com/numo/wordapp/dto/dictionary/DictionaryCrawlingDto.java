package com.numo.wordapp.dto.dictionary;

import java.util.List;

public record DictionaryCrawlingDto(
        String word,
        String definition,
        List<String> definitions
        ) {
}
