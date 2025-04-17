package com.numo.batch.listener;

public record WordBatchEvent(
        Long userId,
        Long wordBookId,
        Long targetWordBookId
) {
}
