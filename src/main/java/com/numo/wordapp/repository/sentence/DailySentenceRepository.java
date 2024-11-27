package com.numo.wordapp.repository.sentence;

import com.numo.wordapp.entity.sentence.DailySentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySentenceRepository extends JpaRepository<DailySentence, Long>, DailySentenceCustomRepository {
}
