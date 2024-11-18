package com.numo.wordapp.repository;

import com.numo.wordapp.entity.word.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {
    List<Folder> findByUser_UserId(Long userId);
    Optional<Folder> findByFolderIdAndUser_UserId(int folderId, Long userId);
}

