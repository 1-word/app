package com.numo.wordapp.service;

import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.model.Folder;

import java.util.List;

public interface FolderService {
    Folder updateByFolder(FolderDto.Request fdto);
    Folder setByFolder(FolderDto.Request fdto);
    List<Folder> getByFolderName(String user_id);

    String removeByFolder(FolderDto.Request fdto);
}
