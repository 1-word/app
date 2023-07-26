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
    // fetch join으로 중복 sql문장 조회 방지, left join으로 synonym에 값이 없어도 출력되도록 함
    // order by로 값 정렬
    @Query("SELECT distinct w FROM Word w left join fetch w.synonyms s where w.userId = ?1 order by w.update_time desc, s.synonym_id asc")
    List<Word> getByAllWord(String user_id);

    @Query("SELECT distinct w FROM Word w left join fetch w.synonyms s where w.userId = ?1 and w.folderId = ?2 order by w.update_time desc, s.synonym_id asc")
    List<Word> getByFolderWord(String userId, int folderId);

   @EntityGraph(attributePaths = {"synonym"})
    List<Word> findAll();

    @Query("SELECT distinct w " +
            "FROM Word w " +
            "left join fetch w.synonyms s " +
            "WHERE w.userId = :user_id " +
            "AND (w.word LIKE %:data% " +
            "OR w.mean LIKE %:data% " +
            "OR w.wread LIKE %:data% " +
            "OR w.memo LIKE %:data% " +
            "OR s.synonym LIKE %:data%) " +
            "order by w.update_time desc, s.synonym_id asc")
   List<Word> getBySearchWord(@Param("user_id") String user_id, @Param("data") String data);
}
