package com.numo.wordapp.repository;

import com.numo.wordapp.model.Word;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    Optional<Word> findByUserIdAndWordId(String user_id, int word_id);
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

    // fetch join으로 중복 sql문장 조회 방지, left join으로 synonym에 값이 없어도 출력되도록 함
    // order by로 값 정렬
    @Query("SELECT distinct w FROM Word w left join fetch w.synonyms s where w.userId = ?1 order by w.wordId desc, s.synonym_id asc")
    //List<Word> getByAllWord(Sort sort);
    List<Word> getByAllWord(String user_id);


   @EntityGraph(attributePaths = {"synonym"})
    List<Word> findAll();

   //검색
   //List<Word> findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(int word_id, String word, String mean, String wread, String memo);

    //검색 쿼리
//   @Query("SELECT distinct w " +
//           "FROM Word w " +
//           "left join fetch w.synonyms s " +
//           "WHERE w.word LIKE CONCAT('%', :data,'%') " +
//           "OR w.mean LIKE CONCAT('%', :data,'%')" +
//           "OR w.wread LIKE CONCAT('%', :data,'%')" +
//           "OR w.memo LIKE CONCAT('%', :data,'%')" +
//           "OR s.synonym LIKE CONCAT('%', :data,'%')" +
//           "order by w.word_id desc, s.synonym_id asc")
    @Query("SELECT distinct w " +
            "FROM Word w " +
            "left join fetch w.synonyms s " +
            "WHERE w.userId = :user_id " +
            "AND (w.word LIKE %:data% " +
            "OR w.mean LIKE %:data% " +
            "OR w.wread LIKE %:data% " +
            "OR w.memo LIKE %:data% " +
            "OR s.synonym LIKE %:data%) " +
            "order by w.wordId desc, s.synonym_id asc")
   List<Word> getBySearchWord(@Param("user_id") String user_id, @Param("data") String data);
}
