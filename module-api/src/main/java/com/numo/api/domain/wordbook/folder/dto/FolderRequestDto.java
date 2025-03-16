package com.numo.api.domain.wordbook.folder.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.folder.Folder;
import lombok.Builder;

@Builder
@Deprecated
public record FolderRequestDto(
        String folderName,
        String memo,
        String color,
        String background
) {
    public Folder toEntity(Long userId) {
        User user = User.builder().userId(userId).build();
        return Folder.builder()
                .user(user)
                .folderName(folderName)
                .memo(memo)
                .color(color)
                .background(background)
                .build();
    }
}
