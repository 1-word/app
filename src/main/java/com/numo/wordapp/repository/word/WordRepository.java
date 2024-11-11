package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.word.WordDto;
import com.numo.wordapp.entity.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer>, WordRepositoryCustom {
    Optional<Word> findByUserIdAndWordId(String user_id, int word_id);
    Slice<Word> findByPageWord(Pageable pageable, WordDto.Read readDto);

//    @Query(value = "SELECT distinct w FROM Word w " +
//            "left join fetch w.folder f " +
//            "left join w.wordDetailMains dm " +
//            "where w.email = ?1 " +
//            "and w.update_time < (select w.update_time from Word w where w.wordId = ?2)" +
//            "order by w.update_time desc")
//    Slice<Word> getByPageWord(Pageable pageable, String user_id, int word_id);

//    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.email = ?1 order by w.update_time desc")
    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.userId = ?1 order by w.update_time desc")
    List<Word> getByAllWord(String user_id);

    @Query("SELECT distinct w FROM Word w left join fetch w.folder f where w.userId = ?1 and f.folderId = ?2 order by w.update_time desc")
    List<Word> getByFolderWord(String userId, int folderId);

   @EntityGraph(attributePaths = {"details"})
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
