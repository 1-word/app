package com.numo.wordapp.repository.folder;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.entity.word.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Integer>, FolderCustomRepository {
    Optional<Folder> findByFolderIdAndUser_UserId(Long folderId, Long userId);
    boolean existsByFolderIdAndUser_UserId(Long folderId, Long userId);

    default Folder findByFolderIdAndUserId(Long folderId, Long userId) {
        return findByFolderIdAndUser_UserId(folderId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}

