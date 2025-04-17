package com.numo.batch.word;

import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.word.Word;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
        TypedQuery<Word> query = em.createQuery(selectWord, Word.class);
        List<Word> words = query.setParameter("wordBookId", wordBookId)
                .setMaxResults(pageable.getPageSize() + 1)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .getResultList();
        List<Long> wordIds = words.stream().map(Word::getWordId).toList();
        Map<Long, List<WordDetail>> detailsMap = getDetailsMap(wordIds);
        words.forEach(w -> w.addWordDetails(detailsMap.get(w.getWordId())));

        return of(words, pageable);
    }

    private Map<Long, List<WordDetail>> getDetailsMap(List<Long> wordIds) {
        String selectDetails = "SELECT d FROM WordDetail d WHERE d.word.wordId IN :wordIds";
        TypedQuery<WordDetail> query = em.createQuery(selectDetails, WordDetail.class);
        List<WordDetail> wordDetails = query.setParameter("wordIds", wordIds)
                .getResultList();
        return wordDetails.stream()
                .collect(Collectors.groupingBy(WordDetail::getWordId));
    }

    private  <T> Slice<T> of(List<T> data, Pageable page) {
        boolean hasNext = false;
        if (data.size() > page.getPageSize()) {
            data.remove(page.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(data, page, hasNext);
    }
}
