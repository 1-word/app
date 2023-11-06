package com.numo.wordapp.repository;

import com.numo.wordapp.model.word.detail.WordDetailSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordDetailSubRepository extends JpaRepository<WordDetailSub, Integer> {
    @Query("SELECT distinct ws FROM WordDetailSub ws where ws.wordDetailMain.word.userId = ?1 order by ws.detailSubId" )
    List<WordDetailSub> getByAllDetailSub(int detailMainId);
}
