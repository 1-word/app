package com.numo.wordapp.repository;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
    default File findFileById(String fileId) {
        return findById(fileId).orElseThrow(
                () -> new CustomException(ErrorCode.FILE_NOT_FOUND)
        );
    }
}
