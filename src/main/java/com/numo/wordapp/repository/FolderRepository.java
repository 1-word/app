package com.numo.wordapp.repository;

import com.numo.wordapp.model.word.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {
    List<Folder> findByUserId(String userId);
    Optional<Folder> findByFolderIdAndUserId(int folderId, String userId);
}
