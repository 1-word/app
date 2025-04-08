package com.numo.api.domain.user.repository.query;

import com.numo.api.domain.user.dto.SearchUserDto;
import com.numo.domain.user.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private QUser qUser = QUser.user;

    public List<SearchUserDto> searchUsers(String searchText, int limit) {
        List<SearchUserDto> users = jpaQueryFactory.select(Projections.constructor(
                        SearchUserDto.class,
                        qUser.userId,
                        qUser.nickname,
                        qUser.email,
                        qUser.profileImagePath
                )).from(qUser)
                .where(likeSearchText(searchText))
                .limit(limit)
                .fetch();
        return users;
    }

    private BooleanExpression likeSearchText(String searchText) {
        if (searchText == null) {
            return null;
        }

       return qUser.nickname.startsWith(searchText).or(qUser.email.startsWith(searchText));
    }
}
