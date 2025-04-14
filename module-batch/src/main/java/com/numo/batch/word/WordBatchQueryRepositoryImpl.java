package com.numo.batch.word;

import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.word.Word;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WordBatchQueryRepositoryImpl implements WordBatchQueryRepository {
    private final EntityManager em;

    public Slice<Word> findWordsBy(Long wordBookId, Pageable pageable) {
        String selectWord = "SELECT w FROM Word w join fetch w.sound join fetch w.user WHERE w.wordBook.id = :wordBookId";
        List<Word> word = em.createQuery(selectWord)
                .setParameter("wordBookId", wordBookId)
                .setMaxResults(pageable.getPageSize() + 1)
                .setFirstResult((pageable.getPageNumber() * pageable.getPageSize()))
                .getResultList();
        List<Long> wordIds = word.stream().map(Word::getWordId).toList();
        String selectDetails = "SELECT d FROM WordDetail d WHERE d.word.wordId IN :wordIds";
        List<WordDetail> wordDetails = em.createQuery(selectDetails)
                .setParameter("wordIds", wordIds)
                .getResultList();
        Map<Long, List<WordDetail>> detailsMap = wordDetails.stream()
                .collect(Collectors.groupingBy(WordDetail::getWordId));
        word.forEach(w -> w.addWordDetails(detailsMap.get(w.getWordId())));

        return of(word, pageable);
    }

    public <T> Slice<T> of(List<T> data, Pageable page) {
        boolean hasNext = false;
        if (data.size() > page.getPageSize()) {
            data.remove(page.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(data, page, hasNext);
    }
}
