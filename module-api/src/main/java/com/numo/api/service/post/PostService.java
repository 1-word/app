package com.numo.api.service.post;

import com.numo.api.comm.exception.CustomException;
import com.numo.api.comm.exception.ErrorCode;
import com.numo.api.comm.page.PageResponse;
import com.numo.api.dto.post.PostListResponseDto;
import com.numo.api.dto.post.PostRequestDto;
import com.numo.api.dto.post.PostResponseDto;
import com.numo.api.repository.post.PostRepository;
import com.numo.api.repository.post.query.PostQueryRepository;
import com.numo.domain.post.Post;
import com.numo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    public PostResponseDto savePost(Long userId, PostRequestDto requestDto) {
        Post post = Post.builder()
                .user(User.builder().userId(userId).build())
                .title(requestDto.title())
                .content(requestDto.content())
                .build();
        return PostResponseDto.of(postRepository.save(post));
    }

    public PostResponseDto getPost(Long postId) {
        return postQueryRepository.findPost(postId);
    }

    public PageResponse<PostListResponseDto> getPostList(Pageable page) {
        Slice<PostListResponseDto> postList = postQueryRepository.findPostList(page);
        return new PageResponse<>(postList);
    }

    @Transactional
    public PostResponseDto updatePost(Long userId, Long postId, PostRequestDto requestDto) {
        Post post = postRepository.findPostBy(postId);
        checkOwner(userId, post);
        post.update(requestDto.title(), requestDto.content());
        return PostResponseDto.of(post);
    }

    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findPostBy(postId);
        checkOwner(userId, post);
        postRepository.delete(post);
    }

    private static void checkOwner(Long userId, Post post) {
        if (!Objects.equals(post.getUser().getUserId(), userId)) {
            throw new CustomException(ErrorCode.POST_NOT_OWNED);
        }
    }
}
