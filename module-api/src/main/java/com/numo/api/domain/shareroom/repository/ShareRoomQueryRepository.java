package com.numo.api.domain.shareroom.repository;

import com.numo.api.domain.shareroom.dto.MyShareRoomListDto;
import com.numo.api.domain.shareroom.dto.ShareRoomListDto;
import com.numo.api.global.comm.page.PageUtil;
import com.numo.domain.shareroom.QShareRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShareRoomQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QShareRoom qShareRoom = QShareRoom.shareRoom;

    /**
     * 쉐어룸 데이터를 가져온다.
     *
     * @param page 페이지
     * @return 페이징 처리된 쉐어룸 데이터
     */
    public Slice<ShareRoomListDto> getShareRooms(Long lastId, Pageable page) {
        List<ShareRoomListDto> shareRooms = jpaQueryFactory.select(Projections.constructor(
                        ShareRoomListDto.class,
                        qShareRoom.id,
                        qShareRoom.wordBook.folderId,
                        qShareRoom.wordBook.user.nickname,
                        qShareRoom.wordBook.folderName,
                        qShareRoom.wordBook.background,
                        qShareRoom.wordBook.color
                )).from(qShareRoom)
                .where(ltLastId(lastId))
                .limit(page.getPageSize() + 1)
                .fetch();
        return PageUtil.of(shareRooms, page);
    }

    private BooleanExpression ltLastId(Long lastId) {
        if (lastId == null) {
            return null;
        }
        return qShareRoom.id.lt(lastId);
    }

    public List<MyShareRoomListDto> getMyShareRooms(Long userId) {
        List<MyShareRoomListDto> myShareRoom = jpaQueryFactory.select(Projections.constructor(
                        MyShareRoomListDto.class,
                        qShareRoom.id,
                        qShareRoom.wordBook.folderId,
                        qShareRoom.wordBook.anyoneBasicRole,
                        qShareRoom.wordBook.memberBasicRole
                )).from(qShareRoom)
                .where(
                        qShareRoom.wordBook.user.userId.eq(userId)
                )
                .fetch();
        return myShareRoom;
    }

}
