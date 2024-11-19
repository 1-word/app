package com.numo.wordapp.dto.folder;

import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.Folder;
import lombok.Builder;

@Builder
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
