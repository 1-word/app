package com.numo.wordapp.repository;

import com.numo.wordapp.model.word.Synonym;
import com.numo.wordapp.model.word.detail.WordDetailMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordDetailMainRepository extends JpaRepository<WordDetailMain, Integer> {
    String sql = "select synonym_id, word_id, synonym, memo, create_time, update_time from synonym";
    @Query(value = sql, nativeQuery = true)
    List<Synonym> getWordDetail();

    @Query("SELECT distinct dm FROM WordDetailMain dm left join fetch dm.wordDetailSub ws inner join fetch dm.wordDetailTitle wt where dm.word.userId=?1 order by dm.detailMainId, ws.detailSubId")
    List<WordDetailMain> getByAllDetail(String userId);

    @Query("DELETE FROM WordDetailMain d where d.word.wordId=:id")
    void deleteWordDetail(@Param(value= "id") int id);
}
