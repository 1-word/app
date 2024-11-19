package com.numo.wordapp.dto.page;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PageDto {
    int current;
    int next;
    boolean hasNext;
    Long lastWordId;

    public PageDto(int pageNumber, boolean hasNext, Long lastWordId) {
        this.current = pageNumber;
        this.hasNext = hasNext;
        this.next = hasNext? pageNumber + 1 : pageNumber;
        this.lastWordId = lastWordId;
    }

}
