package com.numo.wordapp.dto.page;

public record PageRequestDto(
    int current,
    Long lastWordId
) {

}
