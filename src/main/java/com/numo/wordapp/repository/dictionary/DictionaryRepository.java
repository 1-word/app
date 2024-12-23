package com.numo.wordapp.repository.dictionary;

import com.numo.wordapp.entity.dictionary.Dictionary;
import com.numo.wordapp.repository.dictionary.query.DictionaryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DictionaryRepository extends JpaRepository<Dictionary, Long>, DictionaryCustomRepository {
    boolean existsByWord(String word);
    Optional<Dictionary> findByWord(String word);
}
