package com.numo.api.dto.page;

public record PageRequestDto(
    int current,
    Long lastId
) {

}
