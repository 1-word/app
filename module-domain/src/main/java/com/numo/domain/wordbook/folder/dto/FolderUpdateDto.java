package com.numo.domain.wordbook.folder.dto;

import lombok.Builder;

@Builder
@Deprecated
public record FolderUpdateDto(
        String folderName,
        String memo,
        String color,
        String background
) {
}
