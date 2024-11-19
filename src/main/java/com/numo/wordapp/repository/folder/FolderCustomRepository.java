package com.numo.wordapp.repository.folder;

import com.numo.wordapp.entity.word.Folder;

import java.util.List;

public interface FolderCustomRepository {
    List<Folder> getFoldersByUserId(Long userId, Long folderId);
}
