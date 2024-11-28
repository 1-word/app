package com.numo.wordapp.dto.word.detail;

import java.util.List;

public record ReadWordDetailResponseDto(
        Long wordGroupId,
        String groupName,
        List<ReadWordDetailGroupingDto> groups
) {
    public ReadWordDetailResponseDto(ReadWordDetailGroupKey keys, List<ReadWordDetailGroupingDto> groups) {
        this(keys.wordGroupId(), keys.groupName(), groups);
    }
}
