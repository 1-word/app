package com.numo.api.global.comm.page;

import org.springframework.data.domain.PageRequest;

public record PageRequestDto(
    Integer current,
    Integer limit,
    Long lastId
) {
    public PageRequestDto {
        current = current == null? 0 : current;
        limit = limit == null? 30 : limit;
    }

    public PageRequest to() {
        return PageRequest.of(current, limit);
    }
}
