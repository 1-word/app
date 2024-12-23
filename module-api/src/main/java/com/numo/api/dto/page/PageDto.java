package com.numo.api.dto.page;

public record PageDto(
    int current,
    int next,
    boolean hasNext,
    Long lastWordId
) {
    public PageDto(int current, Long lastWordId) {
        this(current, 0, false, lastWordId);
    }
    public PageDto(int pageNumber, boolean hasNext, Long lastWordId) {
        this(pageNumber, hasNext? pageNumber+1 :pageNumber, hasNext, lastWordId);
    }
}
