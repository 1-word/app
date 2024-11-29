package com.numo.wordapp.dto.word.detail;

public record ReadWordDetailGroupKey(
        Long wordId,
        Long wordGroupId,
        String groupName
) {
}
