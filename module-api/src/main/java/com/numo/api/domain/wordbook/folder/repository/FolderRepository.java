package com.numo.api.domain.wordbook.folder.repository;

import com.numo.domain.wordbook.folder.Folder;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Deprecated
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Optional<Folder> findByFolderIdAndUser_UserId(Long folderId, Long userId);
    boolean existsByFolderIdAndUser_UserId(Long folderId, Long userId);
    boolean existsByFolderId(Long folderId);

    default Folder findByFolderIdAndUserId(Long folderId, Long userId) {
        return findByFolderIdAndUser_UserId(folderId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}

