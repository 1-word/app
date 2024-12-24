package com.numo.api.repository.post.query;

import com.numo.api.comm.page.PageUtil;
import com.numo.api.dto.post.PostListResponseDto;
import com.numo.api.dto.post.PostResponseDto;
import com.numo.domain.post.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QPost qPost = QPost.post;

    public PostResponseDto findPost(Long postId) {
        return jpaQueryFactory.select(Projections.constructor(
                PostResponseDto.class,
                qPost.postId,
                qPost.user.nickname,
                qPost.title,
                qPost.content,
                qPost.createTime,
                qPost.updateTime
                ))
                .from(qPost)
                .join(qPost.user)
                .where(
                        qPost.postId.eq(postId)
                )
                .fetchOne();
    }

    public Slice<PostListResponseDto> findPostList(Pageable page) {
        List<PostListResponseDto> posts = jpaQueryFactory.select(Projections.constructor(
                        PostListResponseDto.class,
                        qPost.postId,
                        qPost.user.nickname,
                        qPost.title,
                        qPost.createTime,
                        qPost.updateTime
                ))
                .from(qPost)
                .join(qPost.user)
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

       return PageUtil.of(posts, page);
    }
}
