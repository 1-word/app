package com.numo.wordapp.repository;

import com.numo.wordapp.entity.word.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoundRepository extends JpaRepository<Sound, String> {
    Optional<Sound> findByWord(String word);
    boolean existsByWord(String word);

}
