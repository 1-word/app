package com.numo.api.global.comm.date;

public record DateRequestDto(
        Integer year,
        Integer month,
        Integer day,
        Integer week
) {
}
