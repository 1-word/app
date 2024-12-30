package com.numo.api.domain.wordbook.folder.repository.query;

import com.numo.api.domain.wordbook.folder.dto.FolderResponseDto;

import java.util.List;

public interface FolderCustomRepository {
    List<FolderResponseDto> getFoldersByUserId(Long userId, Long folderId);
}
