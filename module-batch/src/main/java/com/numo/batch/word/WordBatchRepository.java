package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBatchRepository extends JpaRepository<Word, Long>, WordBatchQueryRepository{
//    @EntityGraph(attributePaths = {"wordDetails"})
//    Slice<Word> findByWordBook_Id(Long wordBookId, Pageable pageable);
}
