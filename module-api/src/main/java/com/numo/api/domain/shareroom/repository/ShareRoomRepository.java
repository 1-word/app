package com.numo.api.domain.shareroom.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.shareroom.ShareRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRoomRepository extends JpaRepository<ShareRoom, Long> {
    @EntityGraph(attributePaths = {"wordBook"})
    Optional<ShareRoom> findShareRoomById(Long id);
    default ShareRoom findShareRoomByIdWithWordBook(Long id) {
        return findShareRoomById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    boolean existsByWordBook_FolderId(Long id);
}
