package com.numo.api.domain.post.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p, u " +
            "from Post p " +
            "join fetch p.user u " +
            "where p.postId = :postId")
    Optional<Post> findPost(@Param("postId") Long postId);

    default Post findPostBy(Long postId) {
        return findPost(postId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    Slice<Post> findAllByPostId(Long postId, Pageable page);
}
