package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.word.ReadWordRequestDto;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer>, WordRepositoryCustom {
    Optional<Word> findByUserAndWordId(User user, Long wordId);
    Slice<Word> findWordBy(Pageable pageable, ReadWordRequestDto readDto);
}
