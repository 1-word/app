package com.numo.wordapp.dto.folder;

public record FolderListReadResponseDto(
        FolderResponseDto folders,
        Long count
) {
}
