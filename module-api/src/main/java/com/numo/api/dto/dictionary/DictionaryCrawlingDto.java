package com.numo.api.dto.dictionary;

import java.util.List;

public record DictionaryCrawlingDto(
        String word,
        String definition,
        List<String> definitions
        ) {
}
