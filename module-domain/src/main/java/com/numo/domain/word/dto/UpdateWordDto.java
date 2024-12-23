package com.numo.domain.word.dto;

import com.numo.domain.word.detail.dto.UpdateWordDetailDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateWordDto(
        Long folderId,
        String mean,
        String read,
        String memo,
        String memorization,
        List<UpdateWordDetailDto> details
) {
}
