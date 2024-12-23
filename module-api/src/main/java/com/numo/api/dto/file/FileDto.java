package com.numo.api.dto.file;

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
