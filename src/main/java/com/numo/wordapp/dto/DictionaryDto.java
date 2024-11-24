package com.numo.wordapp.dto;

import com.numo.wordapp.entity.dictionary.Dictionary;
import lombok.Builder;

@Builder
public record DictionaryDto(
        Long dictId,
        String word,
        String wordType,
        String definition
) {
    public static DictionaryDto of(Dictionary dictionary) {
        return DictionaryDto.builder()
                .word(dictionary.getWord())
                .build();
    }

    public Dictionary toEntity() {
        return Dictionary.builder()
                .word(word)
                .wordType(wordType)
                .definition(definition)
                .build();
    }
}
