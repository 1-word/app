package com.numo.wordapp.repository.word.group;

import com.numo.wordapp.entity.word.detail.QWordDetail;
import com.numo.wordapp.entity.word.detail.QWordGroup;
import com.numo.wordapp.entity.word.detail.WordGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WordGroupCustomRepositoryImpl implements WordGroupCustomRepository {
    private final JPAQueryFactory queryFactory;

    QWordGroup qWordGroup = QWordGroup.wordGroup;
    QWordDetail qWordDetail = QWordDetail.wordDetail;

    /**
     * 유저 아이디에 맞는 품사 리스트를 조회한다.
     * @param userId 유저 아이디
     * @return 조회한 품사 리스트
     */
    @Override
    public List<WordGroup> findWordGroupsByUserId(Long userId) {
        List<WordGroup> result = queryFactory
                .selectFrom(qWordGroup)
                .leftJoin(qWordGroup.details)
                .where(qWordGroup.user.userId.eq(userId))
                .fetch();
        return result;
    }

    /**
     * 품사, 유저 아이디에 맞는 품사 정보를 조회한다.
     * 해당하는 detail title 데이터도 조회한다.
     * @param wordGroupId 품사 아이디
     * @param userId 유저 아이디
     * @return 조회한 품사 정보
     */
    @Override
    public Optional<WordGroup> findWordGroupByIdAndUserId(Long wordGroupId, Long userId) {
        WordGroup result = queryFactory
                .selectFrom(qWordGroup)
                .leftJoin(qWordGroup.details)
                .where(
                        qWordGroup.wordGroupId.eq(wordGroupId),
                        qWordGroup.user.userId.eq(userId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
