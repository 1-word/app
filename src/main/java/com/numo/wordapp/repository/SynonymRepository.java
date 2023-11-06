package com.numo.wordapp.repository;

import com.numo.wordapp.model.word.Synonym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynonymRepository extends JpaRepository<Synonym, Integer> {
    String sql = "select synonym_id, word_id, synonym, memo, create_time, update_time from synonym";
    @Query(value = sql, nativeQuery = true)
    List<Synonym> getByAllSynonym();

    String deleteQuery = "delete from synonym where synonym_id=:id";
    @Query(value = deleteQuery, nativeQuery = true)
    void deleteSynonym(@Param(value= "id") int id);
}
