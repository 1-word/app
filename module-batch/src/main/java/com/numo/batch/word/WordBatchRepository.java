package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBatchRepository extends JpaRepository<Word, Long> {
    @EntityGraph(attributePaths = {"wordDetails"})
    Slice<Word> findByWordBook_Id(Long wordBookId, Pageable pageable);
}
