package com.numo.api.domain.post;

import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.domain.post.dto.PostListResponseDto;
import com.numo.api.domain.post.dto.PostRequestDto;
import com.numo.api.domain.post.dto.PostResponseDto;
import com.numo.api.security.service.UserDetailsImpl;
import com.numo.api.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> savePost(@AuthenticationPrincipal UserDetailsImpl user,
                                                    @RequestBody PostRequestDto requestDto) {
        return ResponseEntity.ok(postService.savePost(user.getUserId(), requestDto));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PostListResponseDto>> getPostList(Pageable page) {
        return ResponseEntity.ok(postService.getPostList(page));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@AuthenticationPrincipal UserDetailsImpl user,
                                                      @PathVariable("postId") Long postId,
                                                      @RequestBody PostRequestDto requestDto) {
        return ResponseEntity.ok(postService.updatePost(user.getUserId(), postId, requestDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal UserDetailsImpl user,
                                                   @PathVariable("postId") Long postId) {
        postService.deletePost(user.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }
}
