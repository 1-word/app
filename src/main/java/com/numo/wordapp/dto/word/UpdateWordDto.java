package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.word.detail.UpdateWordDetailRequestDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateWordDto(
        Long folderId,
        String mean,
        String read,
        String memo,
        String memorization,
        List<UpdateWordDetailRequestDto> details
) {
}
