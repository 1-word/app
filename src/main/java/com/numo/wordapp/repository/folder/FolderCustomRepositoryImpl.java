package com.numo.wordapp.repository.folder;

import com.numo.wordapp.dto.folder.FolderResponseDto;
import com.numo.wordapp.entity.word.QFolder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FolderCustomRepositoryImpl implements FolderCustomRepository {
    private final JPAQueryFactory queryFactory;

    QFolder qFolder = QFolder.folder;

    /**
     * 폴더를 조회한다.
     *
     * @param userId   조회할 유저 아이디(Nullable)
     * @param folderId 조회할 폴더 아이디(Nullable)
     * @return 조회한 폴더 데이터
     */
    public List<FolderResponseDto> getFoldersByUserId(Long userId, Long folderId) {
        List<FolderResponseDto> results = queryFactory.select(Projections.constructor(
                        FolderResponseDto.class,
                        qFolder.folderId,
                        qFolder.folderName,
                        qFolder.memo,
                        qFolder.color,
                        qFolder.background
                ))
                .from(qFolder)
                .where(
                        qFolder.user.userId.eq(userId),
                        eqFolderId(folderId)
                )
                .fetch();
        return results;
    }

    private BooleanExpression eqFolderId(Long folderId) {
        if (folderId == null) {
            return null;
        }
        return qFolder.folderId.eq(folderId);
    }
}
