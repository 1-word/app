package com.numo.api.domain.wordbook.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {
    default WordBook findWordBookById(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}

