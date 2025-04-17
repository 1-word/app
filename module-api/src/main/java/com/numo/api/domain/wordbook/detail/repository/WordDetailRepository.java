package com.numo.api.domain.wordbook.detail.repository;

import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.detail.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordDetailRepository extends JpaRepository<WordDetail, Long> {
    boolean existsByWordGroupAndWord_User_UserId(WordGroup wordGroup, Long userId);

    @Transactional
    @Modifying
    @Query(value = "delete from word_detail " +
                    "where word_id in :wordIds",
            nativeQuery = true)
    void deleteByWord_WordIdIn(@Param("wordIds") List<Long> wordIds);
}
