package com.numo.wordapp.repository;

import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynonymRepository extends JpaRepository<Synonym, Integer> {
    String sql = "select synonym_id, word_id, synonym, memo, create_time, update_time from synonym";
    @Query(value = sql, nativeQuery = true)
    List<Synonym> getByAllSynonym();
}
