package com.numo.api.dto.word.detail.read;

public record ReadWordDetailGroupKey(
        Long wordId,
        Long wordGroupId,
        String groupName
) {
}
