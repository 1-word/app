package com.numo.wordapp.repository;

import com.numo.wordapp.model.Word;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {

    //Query(value= "select word_id, word, mean, wread, memo, update_dt from word", nativeQuery = true)
    //List<word>
    /*String sql = "SELECT new map(" +
            "w.word_id as word_id" +
            ", w.word as word" +
            ",w.mean as mean" +
            ",w.wread as wread" +
            ",w.memo as memo" +
            ",w.create_time as create_time" +
            ",w.update_time as update_time)" +
            "FROM word w";*/
    //String sql = "select word_id, word, mean, wread, memo, create_time, update_time from word";
    //@Query(value = sql, nativeQuery = true"")
    //List<Map<String, Object>> getByAllword();

    //fetch join으로 중복 sql문장 조회 방지.
    @Query("SELECT distinct w FROM Word w join fetch w.synonyms")
    List<Word> getByAllWord();


   @EntityGraph(attributePaths = {"synonym"})
    List<Word> findAll();
}
