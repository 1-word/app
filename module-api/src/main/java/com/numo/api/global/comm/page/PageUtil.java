package com.numo.api.global.comm.page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class PageUtil {
    public static <T> Slice<T> of(List<T> data, Pageable page) {
        boolean hasNext = false;
        if (data.size() > page.getPageSize()) {
            data.remove(page.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(data, page, hasNext);
    }
}
