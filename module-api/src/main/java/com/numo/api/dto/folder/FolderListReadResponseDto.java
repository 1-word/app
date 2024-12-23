package com.numo.api.dto.folder;

public record FolderListReadResponseDto(
        FolderResponseDto folders,
        Long count
) {
}
