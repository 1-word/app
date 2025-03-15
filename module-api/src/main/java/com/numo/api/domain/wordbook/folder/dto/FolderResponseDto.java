package com.numo.api.domain.wordbook.folder.dto;

import com.numo.domain.wordbook.folder.Folder;
import lombok.Builder;

@Builder
public record FolderResponseDto(
        Long folderId,
        String nickname,
        String folderName,
        String memo,
        String color,
        String background
) {
    public static FolderResponseDto of(Folder folder) {
        return FolderResponseDto.builder()
                .folderId(folder.getFolderId())
                .nickname(folder.getUser().getNickname())
                .folderName(folder.getFolderName())
                .memo(folder.getMemo())
                .color(folder.getColor())
                .background(folder.getBackground())
                .build();
    }
}
