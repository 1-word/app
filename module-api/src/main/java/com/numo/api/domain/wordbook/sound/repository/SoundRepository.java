package com.numo.api.domain.wordbook.sound.repository;

import com.numo.domain.wordbook.sound.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRepository extends JpaRepository<Sound, String> {
    Sound findByWord(String word);
    boolean existsByWord(String word);
}
