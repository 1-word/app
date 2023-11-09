package com.numo.wordapp.repository;

import com.numo.wordapp.model.word.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    Optional<Word> findByUserIdAndWordId(String user_id, int word_id);
    // fetch join으로 중복 sql문장 조회 방지, left join으로 synonym에 값이 없어도 출력되도록 함
    // order by로 값 정렬
//    @Query("SELECT distinct w FROM Word w left join fetch w.details d left join fetch w.folder f where w.userId = ?1 order by w.update_time desc, d.detailId")
//    List<Word> getByAllWord(String user_id);
    @Query(value = "SELECT distinct w FROM Word w left join fetch w.folder f left join fetch w.wordDetailMains dm where w.userId = ?1 order by w.update_time desc",
    countQuery = "Select count(w) from Word w where w.userId = ?1")
    Page<Word> getByPageWord(Pageable pageable, String user_id);

//    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.userId = ?1 order by w.update_time desc")
    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.userId = ?1 order by w.update_time desc")
    List<Word> getByAllWord(String user_id);

    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.userId = ?1 and f.folderId = ?2 order by w.update_time desc")
    List<Word> getByFolderWord(String userId, int folderId);

   @EntityGraph(attributePaths = {"wordDetails"})
    List<Word> findAll();

    @Query("SELECT distinct w " +
            "FROM Word w " +
            "left join fetch w.wordDetailMains dm " +
            "left join fetch dm.wordDetailTitle wd " +
            "left join fetch w.folder f " +
            "WHERE w.userId = :user_id " +
            "AND (w.word LIKE %:data% " +
            "OR w.mean LIKE %:data% " +
            "OR w.read LIKE %:data% " +
            "OR w.memo LIKE %:data% " +
            "OR dm.content LIKE %:data%) " +
            "order by w.update_time desc, dm.detailMainId")
   List<Word> getBySearchWord(@Param("user_id") String user_id, @Param("data") String data);
}
