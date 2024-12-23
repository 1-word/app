package com.numo.wordapp.repository.word.folder.query;

import com.numo.wordapp.dto.folder.FolderResponseDto;

import java.util.List;

public interface FolderCustomRepository {
    List<FolderResponseDto> getFoldersByUserId(Long userId, Long folderId);
}
