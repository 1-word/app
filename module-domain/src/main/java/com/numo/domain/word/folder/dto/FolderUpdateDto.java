package com.numo.domain.word.folder.dto;

import lombok.Builder;

@Builder
public record FolderUpdateDto(
        String folderName,
        String memo,
        String color,
        String background
) {
}
