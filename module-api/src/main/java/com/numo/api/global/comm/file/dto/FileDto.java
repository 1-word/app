package com.numo.api.global.comm.file.dto;

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
