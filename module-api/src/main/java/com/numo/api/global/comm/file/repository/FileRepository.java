package com.numo.api.global.comm.file.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
    default File findFileById(String fileId) {
        return findById(fileId).orElseThrow(
                () -> new CustomException(ErrorCode.FILE_NOT_FOUND)
        );
    }
}
