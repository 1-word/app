package com.numo.api.domain.wordbook.repository;

import com.numo.domain.wordbook.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {
}

