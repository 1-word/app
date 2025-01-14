package com.numo.api.domain.wordbook.detail.dto.read;

public record ReadWordDetailGroupKey(
        Long wordId,
        Long wordGroupId,
        String groupName
) {
}
