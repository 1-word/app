package com.numo.api.domain.wordbook.folder.dto.read;

import com.numo.api.domain.wordbook.folder.dto.FolderResponseDto;

public record FolderListReadResponseDto(
        FolderResponseDto folders,
        Long count
) {
}