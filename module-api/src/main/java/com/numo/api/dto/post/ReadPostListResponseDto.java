package com.numo.api.dto.post;

import com.numo.api.dto.page.PageDto;

import java.util.List;

public record ReadPostListResponseDto(
        PageDto page,
        List<PostListResponseDto> posts
) {
}
