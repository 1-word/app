package com.numo.api.domain.dictionary.dto;

import com.numo.domain.dictionary.Dictionary;
import lombok.Builder;

@Builder
public record DictionaryDto(
        Long dictId,
        String word,
        String wordType,
        String definition,
        String mean
) {
    public static DictionaryDto of(Dictionary dictionary) {
        return DictionaryDto.builder()
                .word(dictionary.getWord())
                .mean(dictionary.getMean())
                .build();
    }

    public Dictionary toEntity(String mean, String isCrawling) {
        return Dictionary.builder()
                .word(word)
                .wordType(wordType)
                .definition(definition)
                .mean(mean)
                .isCrawling(isCrawling)
                .build();
    }
}
