package com.numo.wordapp.dto.word.detail;

import java.util.List;

public record ReadWordDetailListResponseDto(
        Long wordGroupId,
        String groupName,
        List<ReadWordDetailResponseDto> groups
) {
    public ReadWordDetailListResponseDto(ReadWordDetailGroupKey keys, List<ReadWordDetailResponseDto> groups) {
        this(keys.wordGroupId(), keys.groupName(), groups);
    }
}
