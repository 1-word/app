package com.numo.api.domain.dictionary.dto;

import java.util.List;

public record DictionaryCrawlingDto(
        String word,
        String definition,
        List<String> definitions
        ) {
}
