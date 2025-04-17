package com.numo.api.domain.wordbook.repository.query;

import com.numo.api.domain.wordbook.dto.ShareWordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookSettingDto;
import com.numo.domain.wordbook.QWordBook;
import com.numo.domain.wordbook.QWordBookMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordBookQueryRepository {
    private final JPAQueryFactory queryFactory;

    QWordBook qWordBook = QWordBook.wordBook;
    QWordBookMember qWordBookMember = QWordBookMember.wordBookMember;

    /**
     * 공유 단어장 조회
     *
     * @param userId 유저 아이디
     * @return 공유 단어장 리스트
     */
    public List<ShareWordBookResponseDto> findShareWordBooks(Long userId) {
        List<ShareWordBookResponseDto> shareFolder = queryFactory.select(Projections.constructor(
                        ShareWordBookResponseDto.class,
                        qWordBook.id,
                        qWordBook.user.nickname,
                        qWordBook.name,
                        qWordBook.memo,
                        qWordBook.color,
                        qWordBook.background,
                        qWordBookMember.role,
                        qWordBook.wordCount.totalCount,
                        qWordBook.wordCount.memorizedCount,
                        qWordBook.wordCount.unMemorizedCount
                ))
                .from(qWordBook)
                .join(qWordBookMember).on(qWordBook.id.eq(qWordBookMember.wordBook.id))
                .where(
                        qWordBookMember.user.userId.eq(userId)
                ).fetch();
        return shareFolder;
    }

    /**
     * 폴더를 조회한다.
     *
     * @param userId 조회할 유저 아이디(Nullable)
     * @return 조회한 폴더 데이터
     */
    public List<WordBookResponseDto> findWordBooksByUserId(Long userId) {
        List<WordBookResponseDto> results = queryFactory.select(Projections.constructor(
                        WordBookResponseDto.class,
                        qWordBook.id,
                        qWordBook.user.nickname,
                        qWordBook.name,
                        qWordBook.memo,
                        qWordBook.color,
                        qWordBook.background,
                        qWordBook.wordCount.totalCount,
                        qWordBook.wordCount.memorizedCount,
                        qWordBook.wordCount.unMemorizedCount
                ))
                .from(qWordBook)
                .where(
                        qWordBook.user.userId.eq(userId)
                )
                .fetch();
        return results;
    }

    public WordBookSettingDto findSettingById(Long wordBookId) {
        return queryFactory.select(Projections.constructor(
                        WordBookSettingDto.class,
                        qWordBook.link,
                        qWordBook.isShared,
                        qWordBook.anyoneBasicRole,
                        qWordBook.memberBasicRole
                )).from(qWordBook)
                .where(qWordBook.id.eq(wordBookId))
                .fetchOne();
    }

}
