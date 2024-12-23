package com.numo.wordapp.repository.word.group;

import com.numo.domain.word.detail.QWordGroup;
import com.numo.domain.word.detail.WordGroup;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class WordGroupCustomRepositoryImpl implements WordGroupCustomRepository {
    private final JPAQueryFactory queryFactory;

    QWordGroup qWordGroup = QWordGroup.wordGroup;

    /**
     * 기본 품사 데이터와 유저 아이디에 맞는 품사 리스트를 조회한다.
     * @param userId 유저 아이디
     * @return 조회한 품사 리스트
     */
    @Override
    public List<WordGroup> findWordGroupsByUserId(Long userId) {
        List<WordGroup> result = queryFactory
                .selectFrom(qWordGroup)
                .leftJoin(qWordGroup.user)
                .fetchJoin()
                .where(
                        qWordGroup.user.userId.eq(userId)
                                .or(qWordGroup.defaultGroup.eq("Y"))
                )
                .fetch();
        return result;
    }

    /**
     * 품사, 유저 아이디에 맞는 품사 정보를 조회한다.
     * @param wordGroupId 품사 아이디
     * @param userId 유저 아이디
     * @return 조회한 품사 정보
     */
    @Override
    public Optional<WordGroup> findWordGroupByIdAndUserId(Long wordGroupId, Long userId) {
        WordGroup result = queryFactory
                .selectFrom(qWordGroup)
                .where(
                        qWordGroup.wordGroupId.eq(wordGroupId),
                        qWordGroup.user.userId.eq(userId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsGroup(Long userId, String groupName) {
        Integer fetchOne = queryFactory.selectOne()
                .from(qWordGroup)
                .where(
                        qWordGroup.name.eq(groupName),
                        qWordGroup.user.userId.eq(userId)
                                .or(qWordGroup.defaultGroup.eq("Y"))
                )
                .fetchFirst();
        return fetchOne != null;
    }
}
