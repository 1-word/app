package com.numo.wordapp.repository.word.sound;

import com.numo.domain.word.sound.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRepository extends JpaRepository<Sound, String> {
    Sound findByWord(String word);
    boolean existsByWord(String word);
}
