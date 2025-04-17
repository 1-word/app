package com.numo.api.global.comm.cache;

import lombok.Getter;

@Getter
public enum CacheType {
    WORDBOOK("wordBook", 12, 10000),
    USER("user", 24, 1000),
    ;

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;

    CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
    }
}
