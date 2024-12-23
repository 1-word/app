package com.numo.api.repository.word.folder.query;

import com.numo.api.dto.folder.FolderResponseDto;

import java.util.List;

public interface FolderCustomRepository {
    List<FolderResponseDto> getFoldersByUserId(Long userId, Long folderId);
}
