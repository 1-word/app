package com.numo.api.domain.shareroom.dto;

import com.numo.domain.wordbook.WordBookRole;

public record MyShareRoomListDto(
        Long id,
        Long wordBookId,
        WordBookRole anyoneBasicRole,
        WordBookRole memberBasicRole
) {
}
