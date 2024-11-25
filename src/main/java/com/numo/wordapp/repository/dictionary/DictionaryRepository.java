package com.numo.wordapp.repository.dictionary;

import com.numo.wordapp.entity.dictionary.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DictionaryRepository extends JpaRepository<Dictionary, Long>, DictionaryCustomRepository {
    boolean existsByWord(String word);
}
