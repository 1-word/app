package com.numo.api.domain.wordbook.dto;

import com.numo.domain.wordbook.WordBookRole;

public record WordBookSettingDto(
        String link,
        Boolean isShared,
        WordBookRole anyoneBasicRole,
        WordBookRole memberBasicRole
) {
}
