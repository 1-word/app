package com.numo.api.domain.shareroom.dto;

public record MyShareRoomListDto(
        Long id,
        Long wordBookId,
        String name,
        String background,
        boolean isShared,
        String link
) {
}
