package com.numo.wordapp.repository;

import com.numo.wordapp.entity.word.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRepository extends JpaRepository<Sound, String> {
    Sound findByWord(String word);
    boolean existsByWord(String word);
}
