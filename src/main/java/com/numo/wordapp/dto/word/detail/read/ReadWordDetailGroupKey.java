package com.numo.wordapp.dto.word.detail.read;

public record ReadWordDetailGroupKey(
        Long wordId,
        Long wordGroupId,
        String groupName
) {
}
