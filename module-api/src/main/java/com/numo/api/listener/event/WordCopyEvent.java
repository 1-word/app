package com.numo.api.listener.event;

public record WordCopyEvent(
        Long userId,
        Long wordBookId,
        Long targetWordBookId
) {
}
