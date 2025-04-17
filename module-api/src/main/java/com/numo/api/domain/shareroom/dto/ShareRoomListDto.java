package com.numo.api.domain.shareroom.dto;

public record ShareRoomListDto(
        Long id,
        Long wordBookId,
        String nickname,
        String name,
        String background,
        String color,
        int totalCount
) {
}
