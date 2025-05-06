package com.numo.api.domain.wordbook.repository.query;

import com.numo.api.domain.wordbook.dto.InWordCountDto;
import com.numo.api.domain.wordbook.dto.ShareWordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookSettingDto;
import com.numo.domain.wordbook.QWordBook;
import com.numo.domain.wordbook.QWordBookMember;
import com.numo.domain.wordbook.WordBook;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<WordBookResponseDto> getWordBooksGrouping(Long userId, List<InWordCountDto> wordsCount) {
        List<WordBook> wordbooks = queryFactory.select(qWordBook)
                .from(qWordBook)
                .join(qWordBook.user)
                .fetchJoin()
                .where(qWordBook.user.userId.eq(userId)).fetch();

        Map<Long, InWordCountDto> wordsCountMap = wordsCount.stream().collect(Collectors.toMap(InWordCountDto::getWordBookId, Function.identity()));

        return wordbooks.stream().map(
                wordBook -> {
                    InWordCountDto countDto = wordsCountMap.get(wordBook.getId());
                    int totalCount = 0;
                    int memorizedCount = 0;
                    int unMemorizedCount = 0;
                    if (countDto != null) {
                        totalCount = countDto.getTotalCount() == null ? 0 : countDto.getTotalCount().intValue();
                        memorizedCount = countDto.getMemorizedCount() == null ? 0 : countDto.getMemorizedCount().intValue();
                        unMemorizedCount = countDto.getUnMemorizedCount() == null ? 0 : countDto.getUnMemorizedCount().intValue();
                    }
                    return WordBookResponseDto.builder()
                            .wordBookId(wordBook.getId())
                            .name(wordBook.getName())
                            .nickname(wordBook.getUser().getNickname())
                            .totalCount(totalCount)
                            .memorizedCount(memorizedCount)
                            .unMemorizedCount(unMemorizedCount)
                            .memo(wordBook.getMemo())
                            .color(wordBook.getColor())
                            .background(wordBook.getBackground())
                            .build();
                }
        ).toList();
    }
}
