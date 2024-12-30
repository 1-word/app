package com.numo.api.global.comm.page;

public record PageDto(
    int current,
    int next,
    boolean hasNext,
    Long lastId
) {
    public PageDto(int current, Long lastId) {
        this(current, 0, false, lastId);
    }
    public PageDto(int pageNumber, boolean hasNext, Long lastId) {
        this(pageNumber, hasNext? pageNumber+1 :pageNumber, hasNext, lastId);
    }
}
