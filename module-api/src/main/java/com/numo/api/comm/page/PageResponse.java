package com.numo.api.comm.page;

import org.springframework.data.domain.Slice;

import java.util.List;

public record PageResponse<T>(
        PageDto page,
        List<T> data
){
    public PageResponse(Slice<T> data) {
        this(data, null);
    }

    public PageResponse(Slice<T> data, Long lastId) {
        this(new PageDto(data.getNumber(), data.hasNext(), lastId), data.getContent());
    }
}
