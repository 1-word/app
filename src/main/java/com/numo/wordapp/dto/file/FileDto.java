package com.numo.wordapp.dto.file;

import lombok.Builder;

@Builder
public record FileDto(
        String fileId,
        String oriName,
        String saveName,
        String path,
        String extension
) {
}
