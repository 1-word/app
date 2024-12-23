package com.numo.wordapp.repository.word;

import com.numo.domain.word.detail.WordDetail;
import com.numo.domain.word.detail.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordDetailRepository extends JpaRepository<WordDetail, Long> {
    boolean existsByWordGroupAndWord_User_UserId(WordGroup wordGroup, Long userId);
}
