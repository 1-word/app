package com.numo.domain.wordbook.word.dto;

import com.numo.domain.wordbook.detail.dto.UpdateWordDetailDto;
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
