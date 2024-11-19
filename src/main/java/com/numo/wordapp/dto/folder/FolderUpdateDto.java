package com.numo.wordapp.dto.folder;

import lombok.Builder;

@Builder
public record FolderUpdateDto(
        String folderName,
        String memo,
        String color,
        String background
) {
}
